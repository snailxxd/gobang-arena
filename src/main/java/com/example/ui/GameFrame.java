package com.example.ui;

import com.example.model.room.ChessBoard;
import com.example.model.room.GameRoom;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    private final GameRoom gameRoom;

    public GameFrame(GameRoom gameRoom) {
        this.gameRoom = gameRoom;

        setTitle("五子棋游戏");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        BoardPanel boardPanel = new BoardPanel(gameRoom.getBoard());
        RoomPanel roomPanel = new RoomPanel(gameRoom);

        setLayout(new BorderLayout());

        add(boardPanel);
        add(roomPanel, "East");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
