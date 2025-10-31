package com.booquest.booquest_api.adapter.in.record.web;

import com.booquest.booquest_api.adapter.in.record.web.dto.*;
import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarDayRecord;
import com.booquest.booquest_api.adapter.in.record.web.dto.CalendarRecordResponse;
import com.booquest.booquest_api.application.port.in.record.*;
import com.booquest.booquest_api.common.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/records")
@Tag(name = "Daily Record", description = "오늘의 부업 활동 기록 API")
public class DailyRecordController {
    private final GetRecordSummaryUseCase getRecordSummaryUseCase;
    private final CreateRecordUseCase createRecordUseCase;
    private final GetCalendarRecordsUseCase getCalendarRecordsUseCase;
    private final GetRecordUseCase getRecordUseCase;
    private final UpdateRecordUseCase updateRecordUseCase;
    private final DeleteRecordUseCase deleteRecordUseCase;

    @GetMapping("/summary")
    @Operation(summary = "이번주 부업 활동 기록 요약 조회",
               description = "이번주 부업 활동 기록 여부를 반환합니다. (일 ~ 토)")
    public ApiResponse<RecordSummaryResponse> getRecordSummary() {
        Long userId = getUserId();

        RecordSummaryResponse response = getRecordSummaryUseCase.getRecordSummary(userId);
        return ApiResponse.success("부업 활동 기록 요약 정보가 조회되었습니다.", response);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "오늘의 부업 활동 기록 생성",
            description = "텍스트와 이미지 URL로 오늘의 부업 활동을 기록하고 경험치 5XP를 지급받습니다. 해당 경험치는 하루 1회만 받을 수 있습니다.")
    public ApiResponse<DailyRecordResponse> createRecord(@Valid @ModelAttribute CreateRecordRequest request) {
        Long userId = getUserId();

        DailyRecordResponse response = createRecordUseCase.createRecord(userId, request.getContent(), request.getFile());
        String message = response.isXpGranted() ? "부업 활동 기록이 저장되고 경험치 5XP가 지급되었습니다." : "부업 활동 기록이 저장되었습니다.";

        return ApiResponse.success(message, response);
    }

    @GetMapping("/calendar")
    @Operation(summary = "오늘의 부업 활동 기록 조회 (캘린더 형식)",
               description = "지정된 연월의 기록 현황을 캘린더 형식으로 반환합니다.")
    public ApiResponse<CalendarRecordResponse> getCalendarRecords(@RequestParam int year, @RequestParam int month) {
        Long userId = getUserId();
        CalendarRecordResponse response = getCalendarRecordsUseCase.getCalendarRecords(userId, year, month);
        
        List<CalendarDayRecord> dayRecords = response.getRecords().stream()
            .map(record -> new CalendarDayRecord(record.getDay(), record.isHasRecord(), record.getImageUrl(), record.isXpGranted()))
            .toList();
        
        CalendarRecordResponse calendarRecordResponse = new CalendarRecordResponse(
            response.getYear(),
            response.getMonth(),
            dayRecords
        );
        
        return ApiResponse.success("캘린더 부업 활동 기록이 조회되었습니다.", calendarRecordResponse);
    }

    @GetMapping("/date/{date}")
    @Operation(summary = "오늘의 부업 활동 기록 상세 조회 (날짜 조회)",
               description = "지정된 날짜의 기록을 조회합니다. 기록이 없으면 빈 응답을 반환합니다.</br></br>" +
                       "Date 형식: 2025-10-30")
    public ApiResponse<DailyRecordResponse> getRecordByDate(@PathVariable LocalDate date) {
        Long userId = getUserId();
        DailyRecordResponse response = getRecordUseCase.getRecordByDate(userId, date);
        
        return ApiResponse.success("해당 날짜의 부업 활동 기록이 조회되었습니다.", response);
    }

    @PutMapping(value = "/{recordId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "오늘의 부업 활동 기록 수정",
            description = "부업 활동 기록을 수정합니다.</br>" +
                    "- content만 보내면 내용만 수정되고 기존 이미지는 그대로 둡니다.</br>" +
                    "- file을 업로드하면 기존 이미지는 삭제되고 새 이미지로 교체됩니다. (removeImage=false 여야 함)</br>" +
                    "- removeImage=true 를 보내면 기존 이미지를 삭제합니다. (file과 상관없이)</br>" +
                    "경험치는 더이상 지급되지 않습니다.")
    public ApiResponse<UpdateRecordResponse> updateRecord(@PathVariable Long recordId, @Valid @ModelAttribute UpdateRecordRequest request) {
        Long userId = getUserId();

        UpdateRecordResponse response = updateRecordUseCase.updateRecord(userId, recordId, request);
        String message = "부업 활동 기록이 수정되었습니다.";
        return ApiResponse.success(message, response);
    }

    @DeleteMapping("/{recordId}")
    @Operation(summary = "오늘의 부업 활동 기록 삭제",
            description = "부업 활동 기록을 삭제합니다.</br>" +
                    "- 이 기록이 생성될 때 5XP가 지급되었다면, 삭제 시 5XP가 회수됩니다.</br>" +
                    "- 기록에 이미지가 있으면 NCP Object Storage에서도 함께 삭제됩니다.")
    public ApiResponse<DeleteRecordResponse> deleteRecord(@PathVariable Long recordId) {
        Long userId = getUserId();
        DeleteRecordResponse response = deleteRecordUseCase.deleteRecord(userId, recordId);
        String message = response.getRecordDate() + "부업 활동 기록이 삭제되었습니다. 경험치 5XP가 회수되었습니다.";

        return ApiResponse.success(message, response);
    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }
}
