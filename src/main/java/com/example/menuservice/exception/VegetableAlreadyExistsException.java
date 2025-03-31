package com.example.menuservice.exception;

public class VegetableAlreadyExistsException extends RuntimeException {
    public VegetableAlreadyExistsException(String vegetableName) {
        super("Vegetable already exists: " + vegetableName);
    }
}
