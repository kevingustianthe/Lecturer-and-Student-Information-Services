package com.fikupnvj.restfulapi.tool;

import com.fikupnvj.restfulapi.model.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> responseStatusException(ResponseStatusException exception) {
        ApiResponse<Object> response = new ApiResponse<>(false, exception.getReason(), null);
        return new ResponseEntity<>(response, exception.getStatusCode());
    }
}
