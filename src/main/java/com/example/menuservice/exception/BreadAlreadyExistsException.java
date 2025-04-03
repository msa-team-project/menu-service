package com.example.menuservice.exception;

public class BreadAlreadyExistsException extends RuntimeException {
    public BreadAlreadyExistsException(String breadName) {
        super("Bread already exists: " + breadName);
    }
}
