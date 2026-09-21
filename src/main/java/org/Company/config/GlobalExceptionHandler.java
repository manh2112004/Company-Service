package org.Company.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.CompletionException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> handleResponseStatusException(ResponseStatusException ex) {
        log.warn("Request failed: status={}, message={}", ex.getStatusCode(), ex.getReason());
        return ResponseEntity.status(ex.getStatusCode())
                .body(buildBody(ex.getStatusCode().value(), ex.getReason()));
    }

    @ExceptionHandler(CompletionException.class)
    public ResponseEntity<Map<String, Object>> handleCompletionException(CompletionException ex) {
        Throwable cause = ex.getCause();
        if (cause instanceof ResponseStatusException responseStatusException) {
            return handleResponseStatusException(responseStatusException);
        }
        if (cause instanceof org.axonframework.queryhandling.QueryExecutionException queryExecutionException) {
            return handleQueryExecutionException(queryExecutionException);
        }

        log.error("Async request failed", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống"));
    }

    @ExceptionHandler(org.axonframework.queryhandling.QueryExecutionException.class)
    public ResponseEntity<Map<String, Object>> handleQueryExecutionException(org.axonframework.queryhandling.QueryExecutionException ex) {
        log.warn("Query execution failed: {}", ex.getMessage());
        String msg = ex.getMessage();
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String reason = "Lỗi hệ thống";

        if (msg != null) {
            if (msg.contains("404") || msg.contains("NOT_FOUND")) {
                status = HttpStatus.NOT_FOUND;
                reason = "Không tìm thấy dữ liệu hoặc đối tượng không tồn tại";
            } else if (msg.contains("403") || msg.contains("FORBIDDEN")) {
                status = HttpStatus.FORBIDDEN;
                reason = "Bạn không có quyền thực hiện hành động này";
            } else if (msg.contains("400") || msg.contains("BAD_REQUEST")) {
                status = HttpStatus.BAD_REQUEST;
                reason = "Dữ liệu yêu cầu không hợp lệ";
            }
        }

        return ResponseEntity.status(status)
                .body(buildBody(status.value(), reason));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String message = "Dữ liệu request không hợp lệ";
        if (fieldError != null) {
            message = fieldError.getField() + " không đúng định dạng hoặc giá trị không hợp lệ";
        }

        log.warn("Request validation failed: {}", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(buildBody(HttpStatus.BAD_REQUEST.value(), message));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception ex) {
        log.error("Unexpected request failed", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(buildBody(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Lỗi hệ thống"));
    }

    private Map<String, Object> buildBody(int status, String message) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", LocalDateTime.now());
        body.put("status", status);
        body.put("message", message);
        return body;
    }
}
