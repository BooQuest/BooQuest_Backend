package com.booquest.booquest_api.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.LocalDate;

@ResponseStatus(HttpStatus.CONFLICT)
public class DailyRecordAlreadyExistsException extends RuntimeException {
    private final Long recordId;
    private final LocalDate date;

    private DailyRecordAlreadyExistsException(Long recordId, LocalDate date) {
        super("오늘 기록이 이미 존재합니다.");
        this.recordId = recordId; this.date = date;
    }
    public static DailyRecordAlreadyExistsException with(Long id, LocalDate date) {
        return new DailyRecordAlreadyExistsException(id, date);
    }
}
