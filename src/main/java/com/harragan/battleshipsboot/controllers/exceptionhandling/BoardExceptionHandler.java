package com.harragan.battleshipsboot.controllers.exceptionhandling;

import com.harragan.battleshipsboot.service.exceptions.IllegalBoardPlacementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
@ControllerAdvice
public class BoardExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(IllegalBoardPlacementException.class)
    public ResponseEntity<String> handleExceptions(RuntimeException ex, WebRequest webRequest) {
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.BAD_REQUEST, ex);
        return new ResponseEntity(exceptionResponse.getMessage(), exceptionResponse.getStatus());
    }
}
