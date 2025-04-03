package com.example.menuservice.exception;

public class MenuAlreadyExistsException extends RuntimeException {
    public MenuAlreadyExistsException(String menuName) {
        super("Menu already exists: " + menuName);
    }
}
