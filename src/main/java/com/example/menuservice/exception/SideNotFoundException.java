package com.example.menuservice.exception;

public class SideNotFoundException extends RuntimeException {
    public SideNotFoundException(String sideName) {
        super("Side does not exists: " + sideName);
    }
}
