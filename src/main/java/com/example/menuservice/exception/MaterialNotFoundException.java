package com.example.menuservice.exception;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(String materialName) {
        super("Material does not exists: " + materialName);
    }
}
