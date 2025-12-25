package com.example.service.manager;

import com.example.model.player.Player;
import com.example.model.room.GameRoom;

public class GameManager {
    private static final GameManager INSTANCE = new GameManager();

    private final RoomManager roomManager = RoomManager.getInstance();

    private GameManager() {}

    public static GameManager getInstance() {
        return INSTANCE;
    }

    /**
     * 处理加入请求
     */
    public void handleJoin(Player player, String roomId) throws Exception {
        GameRoom room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new Exception("房间不存在");
        }
        room.join(player);
    }

    /**
     * 处理离开请求
     */
    public void handleLeave(Player player, String roomId) throws Exception {
        GameRoom room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new Exception("房间不存在");
        }
        room.leave(player);
    }

    /**
     * 处理落子请求
     */
    public void handleMove(Player player, String roomId, int x, int y) throws Exception {
        GameRoom room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new Exception("房间不存在");
        }
        room.move(player, x, y);
    }

    /**
     * 处理开始游戏请求
     */
    public void handleStart(String roomId) throws Exception {
        GameRoom room = roomManager.getRoom(roomId);
        if (room == null) {
            throw new Exception("房间不存在");
        }
        room.startGame();
    }
}
