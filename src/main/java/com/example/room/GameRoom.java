package com.example.room;

import com.example.player.Player;
import java.util.Random;

public class GameRoom {
    private final String roomId;    // 房间 id
    private Game game;              // 游戏实例
    private RoomState state;        // 房间状态
    private Player player1;
    private Player player2;
    private Player winner;


    public GameRoom(String roomId) {
        this.roomId = roomId;
        this.state = RoomState.WAITING;
    }

    /**
     * 加入房间
     * @param player 加入的玩家
     * @throws Exception 如果房间已满
     */
    public void join(Player player) throws Exception {
        if (player1 == null) {
            player1 = player;
        } else if (player2 == null) {
            player2 = player;
        } else {
            throw new Exception("房间已满");
        }
    }

    /**
     * 离开房间
     * @param player 离开的玩家
     */
    public void leave(Player player) {
        if (player1 == player) {
            player1 = null;
        } else if (player2 == player) {
            player2 = null;
        } else {
            throw new IllegalArgumentException("玩家不存在");
        }
    }

    /**
     * 开始游戏
     * @throws Exception 如果人数不足
     */
    public void startGame() throws Exception {
        if (player1 == null || player2 == null) {
            throw new Exception("人数不足，无法开始");
        }

        game = new Game();
        state = RoomState.PLAYING;
        winner = null;

        if (player1.getStone() == null || player2.getStone() == null) {
            Random ran = new Random();              // 第一次开始，随机分配颜色
            boolean isBlack = ran.nextBoolean();
            if (isBlack) {
                player1.setStone(Stone.BLACK);
                player2.setStone(Stone.WHITE);
            } else {
                player1.setStone(Stone.WHITE);
                player2.setStone(Stone.BLACK);
            }
        } else {                                    // 反转颜色
            Stone tmp = player1.getStone();
            player1.setStone(player2.getStone());
            player2.setStone(tmp);
        }
    }

    public void move(Player player, int x, int y) throws Exception {
        if (game == null) {
            throw new Exception("游戏尚未开始");
        }
        if (game.isFinished()) {
            throw new Exception("游戏已结束");
        }
        if (game.getCurrentTurn() != player.getStone()) {
            throw new Exception("不是你的回合");
        }

        game.move(x, y);            // 落子

        if (game.isFinished()) {    // 检查游戏状态，判断赢家
            state = RoomState.FINISHED;
            Stone winnerStone = game.getWinner();
            if (winnerStone == player1.getStone()) {
                winner = player1;
            } else if (winnerStone == player2.getStone()) {
                winner = player2;
            }
        }
    }

    public String getRoomId() {
        return roomId;
    }

    public Player getWinner() {
        return winner;
    }

    public RoomState getState() {
        return state;
    }
}
