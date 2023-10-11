package com.ccsw.tutorialcustomer.customer.exception;

public class MyBadAdviceException extends RuntimeException {

    private static final long serialVersionUID = 1162619181129975479L;
    public String message;

    public MyBadAdviceException(String message) {
        super();
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
