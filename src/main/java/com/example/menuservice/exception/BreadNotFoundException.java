package com.example.menuservice.exception;

public class BreadNotFoundException extends RuntimeException {
    public BreadNotFoundException(String breadName) {
        super("Bread does not exists: " + breadName);
    }
}
