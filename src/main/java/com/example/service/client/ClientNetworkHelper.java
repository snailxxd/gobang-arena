package com.example.service.client;

import com.example.model.room.GameRoom;
import com.example.service.network.NetworkPacket;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientNetworkHelper {
    private static final ClientNetworkHelper INSTANCE = new ClientNetworkHelper();

    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private Gson gson = new Gson();
    private ClientEventListener listener;

    private ClientNetworkHelper() {}

    public static ClientNetworkHelper getInstance() {
        return INSTANCE;
    }

    public void setListener(ClientEventListener listener) {
        this.listener = listener;
    }

    public void connect(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new java.io.InputStreamReader(socket.getInputStream()));

        new Thread(this::listen).start();
    }

    public void sendPacket(Object packet) {
        String json = gson.toJson(packet);
        out.println(json);
    }

    private void listen() {
        try {
            String line;
            while ((line = in.readLine()) != null) {
                NetworkPacket packet = gson.fromJson(line, NetworkPacket.class);
                handleServerPacket(packet);
            }
        } catch (IOException e) {
            System.out.println("连接断开" + e.getMessage());
        }
    }

    private void handleServerPacket(NetworkPacket packet) {
        switch (packet.getType()) {
            case SUCCESS: {
                String msg = gson.fromJson(packet.getData(), String.class);
                if (listener != null) {
                    listener.onSuccess(msg);
                }
                break;
            }
            case ERROR: {
                String msg = gson.fromJson(packet.getData(), String.class);
                if (listener != null) {
                    listener.onError(msg);
                }
                break;
            }
            case UPDATE_ROOM: {
                GameRoom room = gson.fromJson(packet.getData(), GameRoom.class);
                if (listener != null) {
                    listener.onRoomUpdate(room);
                }
                break;
            }
            case JOIN_ROOM: {
                if (listener != null) {
                    listener.onJoin();
                }
                break;
            }
            case ROOM_ID: {
                if (listener != null) {
                    listener.onRoomId(gson.fromJson(packet.getData(), String.class));
                }
                break;
            }
        }
    }
}

