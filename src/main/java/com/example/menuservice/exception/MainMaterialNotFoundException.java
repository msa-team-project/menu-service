package com.example.menuservice.exception;

public class MainMaterialNotFoundException extends RuntimeException {
    public MainMaterialNotFoundException(String materialName) {
        super("Material does not exists: " + materialName);
    }
}
