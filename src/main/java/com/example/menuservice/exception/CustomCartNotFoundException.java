package com.example.menuservice.exception;

public class CustomCartNotFoundException extends RuntimeException {
    public CustomCartNotFoundException(String customCartId) {
        super("Custom cart not found for id: " + customCartId);
    }
}
