package org.example.exceptions;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {

        super(message);
    }

    public String getMessage() {
        return super.getMessage();
    }
}