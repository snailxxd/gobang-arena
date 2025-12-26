package com.example.service.server;

import com.example.service.manager.RoomManager;

public class IdelRoomCleaner implements Runnable {
    private final RoomManager roomManager = RoomManager.getInstance();

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(30000);                        // 每30秒检查一次
                roomManager.cleanUpIdleRooms(30000);   // 清理30秒未使用的房间
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }
}
