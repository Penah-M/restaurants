package com.example.ms.exception;

import com.example.ms.dto.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandle {
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ErrorResponse handleNotFound(NotFoundException ex) {

        log.error("Melumat tapilmadi",ex.getMessage());

      return ErrorResponse.builder()
               .message(ex.getMessage())
               .status(HttpStatus.NOT_FOUND.value())
               .timestamp(LocalDateTime.now())
               .build();
    }
}