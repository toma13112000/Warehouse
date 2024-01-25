package org.example.exceptions;

public class BrandNotFoundException extends RuntimeException {
    public BrandNotFoundException(String message) {

        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}