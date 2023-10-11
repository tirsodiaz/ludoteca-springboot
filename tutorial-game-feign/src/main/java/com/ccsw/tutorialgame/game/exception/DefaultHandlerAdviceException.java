package com.ccsw.tutorialgame.game.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class DefaultHandlerAdviceException extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<String> myAdviceException(MyBadAdviceException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
        // return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler
    public ResponseEntity<String> myConflictAdviceException(MyConflictAdviceException e) {
        return new ResponseEntity<String>(e.getMessage(), HttpStatus.CONFLICT);
        // return ResponseEntity.status(409).body(e.getMessage());
    }

}
