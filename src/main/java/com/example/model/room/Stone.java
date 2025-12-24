package com.example.model.room;

/**
 * 棋子枚举类型
 */
public enum Stone {
    EMPTY,      // 空位
    BLACK,      // 白子
    WHITE;       // 黑子

    public static Stone takeTurn(Stone stone) {
        if (stone == Stone.EMPTY) {
            throw new IllegalArgumentException();
        }
        if (stone == Stone.BLACK) {
            return Stone.WHITE;
        } else {
            return Stone.BLACK;
        }
    }
}
