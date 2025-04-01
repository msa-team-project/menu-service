package com.example.menuservice.exception;

public class StoreNotFoundException extends RuntimeException {
    public StoreNotFoundException(int uid) {
        super("Store does not exists: " + uid);
    }
}
