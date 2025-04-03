package com.example.menuservice.exception;

public class VegetableNotFoundException extends RuntimeException {
    public VegetableNotFoundException(String vegetableName) {
        super("Vegetable does not exists: " + vegetableName);
    }
}
