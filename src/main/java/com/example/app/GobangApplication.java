package com.example.app;

import com.example.model.room.GameRoom;
import com.example.service.manager.RoomManager;
import com.example.ui.GameFrame;
import com.example.ui.PlatformFrame;

import javax.swing.*;

public class GobangApplication {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            PlatformFrame frame = new PlatformFrame();
        });
    }
}
