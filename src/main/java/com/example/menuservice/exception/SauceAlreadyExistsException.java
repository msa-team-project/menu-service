package com.example.menuservice.exception;

public class SauceAlreadyExistsException extends RuntimeException {
    public SauceAlreadyExistsException(String sauceName) {
        super("Sauce already exists: " + sauceName);
    }
}
