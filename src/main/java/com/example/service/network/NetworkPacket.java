package com.example.service.network;

import com.google.gson.Gson;

public class NetworkPacket {
    private final MsgType type;
    private final String data;

    public NetworkPacket(MsgType type, String data) {
        this.type = type;
        this.data = data;
    }

    public static NetworkPacket of(MsgType type, Object obj) {
        String json = new Gson().toJson(obj);
        return new NetworkPacket(type, json);
    }

    public MsgType getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
