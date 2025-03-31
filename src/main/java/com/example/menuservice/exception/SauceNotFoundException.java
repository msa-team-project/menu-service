package com.example.menuservice.exception;

public class SauceNotFoundException extends RuntimeException {
    public SauceNotFoundException(String sauceName) {
        super("Sauce does not exists: " + sauceName);
    }
}
