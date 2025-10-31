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

//    @PostMapping
//    @Operation(summary = "오늘의 부업 활동 기록 생성",
//               description = "텍스트와 이미지 URL로 오늘의 부업 활동을 기록하고 경험치 5XP를 지급받습니다. 해당 경험치는 하루 1회만 받을 수 있습니다.</br></br></br>" +
//                       "Request Body 예시 1. 텍스트만 기록</br>" +
//                       "{" +
//                       "  \"content\": \"오늘은 프리랜서 디자인 작업을 완료했습니다.\"," +
//                       "  \"objectKey\": null" +
//                       "}</br></br>" +
//                       "Request Body 예시 2. 텍스트, 이미지 기록</br>" +
//                       "{" +
//                       "  \"content\": \"오늘 부업으로 썸네일 디자인 작업을 끝냈어요.\"," +
//                       "  \"objectKey\": \"records/123/2025-10-30/07c12f1d-78a3-4b4b-8f12-cc93125bde54.jpg\"" +
//                       "}</br></br>" +
//                       "Request Body 예시 3. 이미지만 기록</br>" +
//                       "{" +
//                       "  \"content\": null," +
//                       "  \"objectKey\": \"records/123/2025-10-30/07c12f1d-78a3-4b4b-8f12-cc93125bde54.jpg\"" +
//                       "}</br></br>")
//    public ApiResponse<DailyRecordResponse> createRecord(@Valid @RequestBody CreateRecordRequest request) {
//        Long userId = getUserId();
//
//        DailyRecordResponse response = createRecordUseCase.createRecord(userId, request);
//        String message = response.isXpGranted() ?
//            "기록이 저장되고 경험치 5XP가 지급되었습니다." :
//            "기록이 저장되었습니다.";
//
//        return ApiResponse.success(message, response);
//    }

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

//    @PutMapping("/{recordId}")
//    @Operation(summary = "오늘의 기록 수정",
//            description = "텍스트와 이미지 URL로 오늘의 부업 활동을 기록합니다. 하루 1회만 XP를 받을 수 있습니다.")
//    public ApiResponse<UpdateRecordResponse> updateRecord(@Valid @RequestBody UpdateRecordRequest request) {
//        Long userId = getUserId();
//
//        UpdateRecordResponse response = updateRecordUseCase.updateRecord(userId, request);
//        String message = "기록이 수정되었습니다.";
//        return ApiResponse.success(message, response);
//    }

//    @DeleteMapping("/{recordId}")
//    @Operation(summary = "부업 활동 기록 삭제",
//            description = "부업 활동 기록을 삭제합니다.")
//    public ApiResponse<DeleteRecordResponse> deleteRecord(@PathVariable Long recordId) {
//        Long userId = getUserId();
//        DeleteRecordResponse response = deleteRecordUseCase.deleteRecord(userId, recordId);
//        String message = response.getRecordDate() + "일자 기록이 삭제되었습니다.";
//
//        return ApiResponse.success(message, response);
//    }

//    @GetMapping("/recent")
//    @Operation(summary = "최근 기록 목록 조회",
//               description = "최근 기록들을 날짜 내림차순으로 조회합니다.")
//    public ApiResponse<List<DailyRecordResponse>> getRecentRecords(
//            @RequestParam(defaultValue = "10") int limit) {
//        Long userId = getUserId();
//        List<DailyRecordResponse> responses = dailyRecordUseCases.getRecentRecords(userId, limit);
//
//        List<DailyRecordResponse> dtos = responses.stream()
//            .map(response -> new DailyRecordResponse(
//                response.id(),
//                response.recordDate(),
//                response.content(),
//                response.imageUrl(),
//                response.xpGranted(),
//                response.xpAmount()
//            ))
//            .toList();
//
//        return ApiResponse.success("최근 기록 목록이 조회되었습니다.", dtos);
//    }

    private Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return Long.parseLong(auth.getName());
    }
}
