package com.example.menuservice.exception;

public class MainMaterialAlreadyExistsException extends RuntimeException {
    public MainMaterialAlreadyExistsException(String materialName) {
        super("Material already exists: " + materialName);
    }
}
