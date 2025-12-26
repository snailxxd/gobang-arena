package com.example.ui;

import com.example.model.player.Player;
import com.example.model.room.GameRoom;
import com.example.model.room.Stone;

import javax.swing.*;
import java.awt.*;

import static com.example.utils.Utils.*;

public class RoomPanel extends JPanel {
    private GameRoom gameRoom;

    public RoomPanel(GameRoom gameRoom) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(ROOM_PANEL_WIDTH, BOARD_PANEL_LENGTH));

        JLabel player1Label = new JLabel("玩家 1: " + nameOrEmpty(gameRoom.getPlayer1()));
        JLabel player2Label = new JLabel("玩家 2: " + nameOrEmpty(gameRoom.getPlayer2()));
        JLabel player1Color = new JLabel("执子: " + colorOrEmpty(gameRoom.getPlayer1()));
        JLabel player2Color = new JLabel("执子: " + colorOrEmpty(gameRoom.getPlayer2()));
        JLabel statusLabel = new JLabel("房间状态: " + gameRoom.getState().toString());
        JButton startBtn = new JButton("开始游戏");
        JButton exitBtn = new JButton("退出房间");

        Dimension btnSize = new Dimension(120, 40);
        startBtn.setMaximumSize(btnSize);
        exitBtn.setMaximumSize(btnSize);

        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Color.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Color.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(50));
        add(player1Label);
        add(player1Color);
        add(Box.createVerticalStrut(80)); // 添加垂直间距
        add(player2Label);
        add(player2Color);
        add(Box.createVerticalStrut(80)); // 添加垂直间距
        add(statusLabel);
        add(Box.createVerticalStrut(100)); // 添加垂直间距
        add(startBtn);
        add(Box.createVerticalStrut(100));
        add(exitBtn);
    }

    private String nameOrEmpty(Player player) {
        return player != null ? player.getName() : "等待加入...";
    }

    private String colorOrEmpty(Player player) {
        return player != null ? (player.getStone() == Stone.BLACK? "黑子" : "白子") : "未分配";
    }
}
