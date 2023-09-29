package com.ccsw.tutorialbooking.booking.exceptions;

public class MyConflictAdviceException extends RuntimeException {

    private static final long serialVersionUID = -5613528564402812966L;
    public String message;

    public MyConflictAdviceException(String message) {
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
