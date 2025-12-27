package com.example.ui;

import com.example.model.room.GameRoom;
import com.example.service.client.ClientEventListener;
import com.example.service.client.ClientNetworkHelper;
import com.example.service.network.MsgType;
import com.example.service.network.NetworkPacket;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame implements ClientEventListener {
    private GameRoom gameRoom;
    private RoomPanel roomPanel;
    private BoardPanel boardPanel;
    ClientNetworkHelper clientNetworkHelper = ClientNetworkHelper.getInstance();
    PlatformFrame lobbyFrame;

    public GameFrame(GameRoom gameRoom, PlatformFrame lobbyFrame) {
        this.gameRoom = gameRoom;
        this.lobbyFrame = lobbyFrame;

        ClientNetworkHelper.getInstance().setListener(this);

        setTitle("五子棋游戏");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        boardPanel = new BoardPanel(gameRoom.getBoard());
        roomPanel = new RoomPanel(gameRoom);

        setLayout(new BorderLayout());

        add(boardPanel);
        add(roomPanel, "East");

        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        clientNetworkHelper.sendPacket(NetworkPacket.of(MsgType.UPDATE_ROOM, null));
    }

    public void returnToLobby() {
        this.dispose();
        ClientNetworkHelper.getInstance().setListener(lobbyFrame);
        lobbyFrame.setVisible(true);
    }

    public void updateRoom(GameRoom gameRoom) {
        boolean isFinished = this.gameRoom.getWinner() != null;
        this.gameRoom = gameRoom;
        roomPanel.updateRoom(gameRoom);
        boardPanel.updateBoard(gameRoom.getBoard());
        repaint();
        if (gameRoom.getWinner() != null && !isFinished) {
            String winnerName = gameRoom.getWinner().getName();
            JOptionPane.showMessageDialog(this, "游戏结束！获胜者是: " + winnerName, "游戏结束", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void onRoomUpdate(GameRoom gameRoom) {
        SwingUtilities.invokeLater(() -> {
            updateRoom(gameRoom);
        });
    }

    @Override
    public void onSuccess(String msg) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, msg, "成功", JOptionPane.PLAIN_MESSAGE);
        });
    }

    @Override
    public void onError(String msg) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, msg, "错误", JOptionPane.ERROR_MESSAGE);
        });
    }

    @Override
    public void onJoin() {}

    @Override
    public void onRoomId(String roomId) {}
}
