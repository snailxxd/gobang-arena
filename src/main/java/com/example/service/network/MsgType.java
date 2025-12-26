package com.example.service.network;

public enum MsgType {
    LOGIN,
    LOGOUT,
    CREATE_ROOM,
    JOIN_ROOM,
    GAME_START,
    LEAVE_ROOM,
    MOVE,

    ERROR,
    SUCCESS,
    ROOM_ID,
    UPDATE_ROOM
}
