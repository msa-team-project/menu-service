package com.example.menuservice.exception;

public class CheeseAlreadyExistsException extends RuntimeException {
    public CheeseAlreadyExistsException(String CheeseName) {
        super("Cheese already exists: " + CheeseName);
    }
}
