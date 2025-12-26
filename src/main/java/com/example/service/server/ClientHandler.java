package com.example.service.server;

import com.example.model.player.Player;
import com.example.model.room.GameRoom;
import com.example.service.manager.GameManager;
import com.example.service.manager.PlayerManager;
import com.example.service.manager.RoomManager;
import com.example.service.network.*;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private final GameManager gameManager = GameManager.getInstance();
    private final RoomManager roomManager = RoomManager.getInstance();
    private final PlayerManager playerManager = PlayerManager.getInstance();

    private final BufferedReader in;
    private final PrintWriter out;
    private final Gson gson = new Gson();

    private final Socket socket;
    private Player player;

    public ClientHandler(Socket socket) throws IOException {
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
    }

    @Override
    public void run() {
        try {
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                try {
                    NetworkPacket packet = gson.fromJson(inputLine, NetworkPacket.class);
                    handlePacket(packet);
                } catch (Exception e) {
                    sendPacket(NetworkPacket.of(MsgType.ERROR, e.getMessage()));
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            if (player != null) {
                try { gameManager.handleLeave(player, player.getRoomId()); } catch (Exception ignored) {}
                playerManager.removePlayer(player.getId());
            }
            try { socket.close(); } catch (IOException ignored) {}
        }
    }

    private void handlePacket(NetworkPacket packet) throws Exception {
        switch (packet.getType()) {
            case LOGIN: {
                PlayerRequest req = gson.fromJson(packet.getData(), PlayerRequest.class);
                this.player = playerManager.createPlayer(req.getName());
                playerManager.registerClientHandler(player, this);
                break;
            }
            case LOGOUT: {
                playerManager.removePlayer(player.getId());
                socket.close();
                break;
            }
            case CREATE_ROOM: {
                GameRoom room = roomManager.createRoom();
                sendPacket(NetworkPacket.of(MsgType.ROOM_ID, "房间ID: " + room.getRoomId()));    // 通知客户端 roomId
                break;
            }
            case JOIN_ROOM: {
                RoomRequest req = gson.fromJson(packet.getData(), RoomRequest.class);
                gameManager.handleJoin(player, req.getRoomId());
                System.out.println("Player " + player.getId() + " joined room " + player.getRoomId());
                sendPacket(NetworkPacket.of(MsgType.JOIN_ROOM, "Joined room " + player.getRoomId()));
                broadcastToRoom(NetworkPacket.of(MsgType.UPDATE_ROOM, roomManager.getRoom(player.getRoomId())), player.getRoomId());
                break;
            }
            case LEAVE_ROOM: {
                String roomId = player.getRoomId();
                System.out.println("Player " + player.getId() + " left room " + roomId);
                gameManager.handleLeave(player, player.getRoomId());
                broadcastToRoom(NetworkPacket.of(MsgType.UPDATE_ROOM, roomManager.getRoom(roomId)), roomId);
                System.out.println("Broadcasted");
                break;
            }
            case GAME_START: {
                gameManager.handleStart(player.getRoomId());
                System.out.println("Room " + player.getRoomId() + " started game");
                broadcastToRoom(NetworkPacket.of(MsgType.SUCCESS, "Game started"), player.getRoomId());
                broadcastToRoom(NetworkPacket.of(MsgType.UPDATE_ROOM, roomManager.getRoom(player.getRoomId())), player.getRoomId());
                break;
            }
            case MOVE: {
                MoveRequest req = gson.fromJson(packet.getData(), MoveRequest.class);
                gameManager.handleMove(player, player.getRoomId(), req.getX(), req.getY());
                broadcastToRoom(NetworkPacket.of(MsgType.UPDATE_ROOM, roomManager.getRoom(player.getRoomId())), player.getRoomId());
                break;
            }
            case UPDATE_ROOM: {
                GameRoom room = roomManager.getRoom(player.getRoomId());
                if (room != null) {
                    sendPacket(NetworkPacket.of(MsgType.UPDATE_ROOM, room));
                }
                break;
            }
        }
    }

    private void sendPacket(NetworkPacket packet) {
        out.println(gson.toJson(packet));
    }

    private void broadcastToRoom(NetworkPacket packet, String roomId) throws Exception {
        GameRoom room = roomManager.getRoom(roomId);
        if (room.getPlayer1() != null) {
            PlayerManager.getInstance().getHandler(room.getPlayer1().getId()).sendPacket(packet);
        }
        if (room.getPlayer2() != null) {
            PlayerManager.getInstance().getHandler(room.getPlayer2().getId()).sendPacket(packet);
        }
    }
}
