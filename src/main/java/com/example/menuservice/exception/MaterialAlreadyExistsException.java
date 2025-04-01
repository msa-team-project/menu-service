package com.example.menuservice.exception;

public class MaterialAlreadyExistsException extends RuntimeException {
    public MaterialAlreadyExistsException(String materialName) {
        super("Material already exists: " + materialName);
    }
}
