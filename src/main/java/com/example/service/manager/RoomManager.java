package com.example.service.manager;

import com.example.model.room.GameRoom;
import com.example.model.room.RoomState;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class RoomManager {
    private static final RoomManager INSTANCE = new RoomManager();

    private final Map<String, GameRoom> rooms = new ConcurrentHashMap<>();

    private RoomManager() {}

    public RoomManager getInstance() {
        return INSTANCE;
    }

    /**
     * 创建一个新房间
     * @return 创建好的房间
     */
    public GameRoom createRoom() {
        String roomId;
        do {
            roomId = UUID.randomUUID().toString().substring(0, 8);
        } while (rooms.containsKey(roomId));
        GameRoom room = new GameRoom(roomId);
        rooms.put(roomId, room);
        System.out.println("Room created: " + roomId);
        return room;
    }

    /**
     * 查找房间
     */
    public GameRoom getRoom(String roomId) {
        return rooms.get(roomId);
    }

    /**
     * 移除房间
     */
    public void removeRoom(String roomId) {
        rooms.remove(roomId);
        System.out.println("Room removed: " + roomId);
    }

    /**
     * 清理所有长时间无活动的空闲房间
     * @param timeoutMills 超时限制 (ms)
     */
   public void cleanUpIdleRooms(long timeoutMills) {
        long now = System.currentTimeMillis();
        // 找到超时空闲房间 id
        List<String> idleRoomIds = rooms.values().stream()
                .filter(room -> (now - room.getLastActiveTime()) > timeoutMills && room.getState() != RoomState.PLAYING)
                .map(GameRoom::getRoomId)
                .toList();
        for (String roomId : idleRoomIds) {
            rooms.remove(roomId);
        }
   }
}
