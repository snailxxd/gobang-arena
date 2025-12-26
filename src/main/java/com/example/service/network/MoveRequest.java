package com.example.service.network;

public class MoveRequest {
    private final int x;
    private final int y;

    public MoveRequest(String roomId, int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
