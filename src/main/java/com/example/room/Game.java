package com.example.room;

import static com.example.utils.Utils.*;

public class Game {
    private final ChessBoard board; // 棋盘
    private Stone currentTurn;      // 本轮棋子颜色
    private boolean finished;       // 游戏状态
    private Stone winner;           // 赢家棋子颜色

    public Game() {
        board = new ChessBoard();
        currentTurn = Stone.BLACK;
        finished = false;
        winner = null;
    }

    /**
     * 在指定位置落子，并判断是否胜利
     * @param x 落子行号
     * @param y 落子列号
     */
    public void move(int x, int y) {
        board.move(x, y, currentTurn);
        if (checkWin(x, y)) {
            finished = true;
            winner = currentTurn;   // 当前颜色获胜
            return;
        } else if (board.getStoneCounts() == CHESS_BOARD_SIZE * CHESS_BOARD_SIZE) {
            finished = true;
            winner = null;          // 平局
            return;
        }
        currentTurn = Stone.takeTurn(currentTurn);
    }

    public boolean isFinished() {
        return finished;
    }

    public Stone getCurrentTurn() {
        return currentTurn;
    }

    public Stone getWinner() {
        return winner;
    }

    /**
     * 检查在 x，y 处落子后是否胜利
     * @param x 落子行号
     * @param y 落子列号
     * @return 胜利返回 true，反之 false
     */
    private boolean checkWin(int x, int y) {
        int[][] directions = {
                {0, 1},     // 行方向
                {1, 0},     // 列方向
                {1, 1},     // 正对角线方向
                {1, -1}     // 副对角线方向
        };
        for (int[] direction : directions) {
            int dx = direction[0];
            int dy = direction[1];

            if (1 + countConsecutive(x, y, dx, dy) + countConsecutive(x, y, -dx, -dy) >= 5) {
                return true;
            }
        }
        return false;
    }

    /**
     * 辅助方法，数一个方向上的连续棋子数
     * @param x 起始行号
     * @param y 起始列号
     * @param dx 行方向
     * @param dy 列方向
     * @return 不包括起始棋子的连续棋子数
     */
    private int countConsecutive(int x, int y, int dx, int dy) {
        int count = 0;
        for (int i = 1; i < 5; i++) {
            int row = x - i * dx;
            int col = y - i * dy;
            if (board.inBoard(row, col) && board.getSquare(row, col) == currentTurn) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
