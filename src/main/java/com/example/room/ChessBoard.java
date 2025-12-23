package com.example.room;

import static com.example.utils.Utils.*;

public class ChessBoard {
    private final Stone[][] chessBoard;     // 棋盘
    private int stoneCounts;                // 棋盘上棋子总数

    public ChessBoard() {
        chessBoard = new Stone[CHESS_BOARD_SIZE][CHESS_BOARD_SIZE];
        stoneCounts = 0;
        for (int x = 0; x < CHESS_BOARD_SIZE; x++) {
            for (int y = 0; y < CHESS_BOARD_SIZE; y++) {
                chessBoard[x][y] = Stone.EMPTY;
            }
        }
    }

    /**
     * 在指定的棋盘格落子
     * @param row 行
     * @param col 列
     * @param stone 棋子颜色
     */
    public void move(int row, int col, Stone stone) {
        if (stone == Stone.EMPTY || stone == null) {
            throw new IllegalArgumentException("Invalid stone");
        }
        if (inBoard(row, col)) {
            throw new IllegalArgumentException("Invalid square");
        }
        chessBoard[row][col] = stone;
        stoneCounts++;
    }

    public Stone getSquare(int row, int col) {
        return chessBoard[row][col];
    }

    public int getStoneCounts() {
        return stoneCounts;
    }

    public boolean inBoard(int row, int col) {
        return row >= 0 && row < CHESS_BOARD_SIZE && col >= 0 && col < CHESS_BOARD_SIZE;
    }
}
