package com.example.backend.Exception;


import com.example.backend.Auth.AuthFailException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(AuthFailException.class)
    public ResponseEntity<Map<String, Object>> authFail(AuthFailException e) throws IOException {
        Map<String, Object> response = new HashMap<>();
        response.put("isSuccess",false);
        response.put("error", e.getMessage());
        return ResponseEntity.badRequest().body(response);
    }
}
