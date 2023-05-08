package com.ccsw.tutorialcustomer.customer;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}
