package com.booquest.booquest_api.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 1) NotFound
    @ExceptionHandler(org.springframework.data.crossstore.ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFound(Exception ex) {
        log.error("[NOT_FOUND] {}", ex.toString(), ex); // ★ 스택 전체 출력
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "ok", false,
                        "status", 404,
                        "error", "NOT_FOUND",
                        "message", safeMsg(ex)
                ));
    }

    // 2) 토큰 에러
    @ExceptionHandler(TokenException.class)
    public ResponseEntity<Map<String, Object>> handleTokenException(TokenException ex) {
        log.error("[UNAUTHORIZED] {}", ex.toString(), ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "ok", false,
                        "status", 401,
                        "error", "UNAUTHORIZED",
                        "message", safeMsg(ex)
                ));
    }

    // 3) 그 외 모든 예외
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        log.error("[INTERNAL_ERROR] {}", ex.toString(), ex);

        Map<String, Object> payload = Map.of(
                "ok", false,
                "status", 500,
                "error", ex.getClass().getSimpleName(),
                "message", safeMsg(ex)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);
    }

    private String safeMsg(Throwable t) {
        String m = t.getMessage();
        return (m == null || m.isBlank()) ? t.getClass().getSimpleName() : m;
    }
}
