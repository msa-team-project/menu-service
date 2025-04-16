package com.example.menuservice.exception;

public class CustomCartAlreadyExistsException extends RuntimeException {
    public CustomCartAlreadyExistsException(Long cartId) {
        super("Custom cart already exists for cartId: " + cartId);
    }
}
