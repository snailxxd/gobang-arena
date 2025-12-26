package com.example.ui;

import com.example.model.player.Player;
import com.example.model.room.GameRoom;
import com.example.model.room.Stone;
import com.example.service.client.ClientNetworkHelper;
import com.example.service.network.MsgType;
import com.example.service.network.NetworkPacket;

import javax.swing.*;
import java.awt.*;

import static com.example.model.room.RoomState.PLAYING;
import static com.example.utils.Utils.*;

public class RoomPanel extends JPanel {
    private GameRoom gameRoom;

    private ClientNetworkHelper clientNetworkHelper = ClientNetworkHelper.getInstance();

    JLabel roomId;
    JLabel player1Label;
    JLabel player2Label;
    JLabel player1Color;
    JLabel player2Color;
    JLabel statusLabel;

    public RoomPanel(GameRoom gameRoom) {
        this.gameRoom = gameRoom;

        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(ROOM_PANEL_WIDTH, BOARD_PANEL_LENGTH));

        roomId = new JLabel("Room ID: " + gameRoom.getRoomId());
        player1Label = new JLabel("玩家 1: " + nameOrEmpty(gameRoom.getPlayer1()));
        player2Label = new JLabel("玩家 2: " + nameOrEmpty(gameRoom.getPlayer2()));
        player1Color = new JLabel("执子: " + colorOrEmpty(gameRoom.getPlayer1()));
        player2Color = new JLabel("执子: " + colorOrEmpty(gameRoom.getPlayer2()));
        statusLabel = new JLabel("房间状态: " + gameRoom.getState().toString());
        JButton startBtn = new JButton("开始游戏");
        JButton exitBtn = new JButton("退出房间");

        Dimension btnSize = new Dimension(120, 40);
        startBtn.setMaximumSize(btnSize);
        exitBtn.setMaximumSize(btnSize);

        roomId.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Label.setAlignmentX(Component.CENTER_ALIGNMENT);
        player1Color.setAlignmentX(Component.CENTER_ALIGNMENT);
        player2Color.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        startBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        add(Box.createVerticalStrut(20));
        add(roomId);
        add(Box.createVerticalStrut(30));
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

        startBtn.addActionListener(e -> {startGame();});
        exitBtn.addActionListener(e -> {exitRoom();});
    }

    public void updateRoom(GameRoom room) {
        gameRoom = room;
        refresh();
    }

    private void refresh() {
        roomId.setText(gameRoom.getRoomId());
        player1Label.setText(nameOrEmpty(gameRoom.getPlayer1()));
        player2Label.setText(nameOrEmpty(gameRoom.getPlayer2()));
        player1Color.setText(colorOrEmpty(gameRoom.getPlayer1()));
        player2Color.setText(colorOrEmpty(gameRoom.getPlayer2()));
        statusLabel.setText(gameRoom.getState().toString());
        revalidate();
        repaint();
    }

    private String nameOrEmpty(Player player) {
        return player != null ? player.getName() : "等待加入...";
    }

    private String colorOrEmpty(Player player) {
        return gameRoom.getState() == PLAYING && player != null ? (player.getStone() == Stone.BLACK? "黑子" : "白子") : "未分配";
    }

    private void startGame() {
        clientNetworkHelper.sendPacket(NetworkPacket.of(MsgType.GAME_START, null));
    }

    private void exitRoom() {
        clientNetworkHelper.sendPacket(NetworkPacket.of(MsgType.LEAVE_ROOM, null));
        GameFrame topFrame = (GameFrame) SwingUtilities.getWindowAncestor(this);
        topFrame.returnToLobby();
    }
}
