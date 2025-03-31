package com.example.menuservice.exception;

public class CheeseNotFoundException extends RuntimeException {
    public CheeseNotFoundException(String cheeseName) {
        super("Cheese does not exists: " + cheeseName);
    }
}
