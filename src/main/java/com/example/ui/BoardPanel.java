package com.example.ui;

import com.example.model.room.ChessBoard;
import com.example.model.room.Stone;
import com.example.service.client.ClientNetworkHelper;
import com.example.service.network.MoveRequest;
import com.example.service.network.MsgType;
import com.example.service.network.NetworkPacket;

import javax.swing.*;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static com.example.utils.Utils.*;

public class BoardPanel extends JPanel {
    private ChessBoard board;
    private final ClientNetworkHelper clientNetworkHelper = ClientNetworkHelper.getInstance();

    public BoardPanel(ChessBoard board) {
        this.board = board;
        setPreferredSize(new Dimension(BOARD_PANEL_LENGTH, BOARD_PANEL_LENGTH));

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleMouseClick(e);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 填充背景色
        g.setColor(new Color(217, 182, 120));
        g.fillRect(0, 0, getWidth(), getHeight());
        g.setColor(Color.black);

        // 画棋盘
        for (int i = 0; i <= CHESS_BOARD_SIZE; i++) {
            g.drawLine(MARGIN, MARGIN + i * CELL_SIZE,
                    MARGIN + CHESS_BOARD_SIZE * CELL_SIZE, MARGIN + i * CELL_SIZE);
            g.drawLine(MARGIN + i * CELL_SIZE, MARGIN,
                    MARGIN + i * CELL_SIZE, MARGIN + CHESS_BOARD_SIZE * CELL_SIZE);
        }

        // 画棋子
        for (int x = 0; x < CHESS_BOARD_SIZE; x++) {
            for (int y = 0; y < CHESS_BOARD_SIZE; y++) {
                if (board.getSquare(x, y) != Stone.EMPTY) {
                    if (board.getSquare(x, y) == Stone.BLACK) {
                        g.setColor(java.awt.Color.BLACK);
                    } else {
                        g.setColor(java.awt.Color.WHITE);
                    }
                    g.fillOval(MARGIN + x * CELL_SIZE + CELL_SIZE /2 - STONE_RADIUS,
                            MARGIN + y * CELL_SIZE + CELL_SIZE /2 - STONE_RADIUS,
                            STONE_RADIUS * 2, STONE_RADIUS * 2);
                }
            }
        }
    }

    public void updateBoard(ChessBoard board) {
        this.board = board;
        repaint();
    }

    private void handleMouseClick(java.awt.event.MouseEvent e) {
        int row = (e.getX() - MARGIN) / CELL_SIZE;
        int col = (e.getY() - MARGIN) / CELL_SIZE;
        if (!board.inBoard(row, col)) {     // 点击位置不在棋盘内
            return;
        }
        MoveRequest moveRequest = new MoveRequest(null, row, col);
        NetworkPacket packet = NetworkPacket.of(MsgType.MOVE, moveRequest);
        clientNetworkHelper.sendPacket(packet);
    }
}
