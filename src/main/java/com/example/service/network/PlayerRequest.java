package com.example.service.network;

public class PlayerRequest {
    private final String name;

    public PlayerRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
