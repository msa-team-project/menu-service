package com.example.menuservice.exception;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(String menuName) {
        super("Menu does not exists: " + menuName);
    }
}
