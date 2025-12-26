package com.example.service.network;

public class RoomRequest {
    private final String roomId;

    public RoomRequest(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomId() {
        return roomId;
    }
}
