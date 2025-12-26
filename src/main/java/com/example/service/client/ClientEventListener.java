package com.example.service.client;

import com.example.model.room.GameRoom;

public interface ClientEventListener {
    void onRoomUpdate(GameRoom room);
    void onSuccess(String msg);
    void onError(String msg);
    void onJoin();
    void onRoomId(String roomId);
}
