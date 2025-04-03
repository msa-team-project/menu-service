package com.example.menuservice.exception;

public class SideAlreadyExistsException extends RuntimeException {
    public SideAlreadyExistsException(String sideName) {
        super("Side already exists: " + sideName);
    }
}
