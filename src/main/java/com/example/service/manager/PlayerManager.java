package com.example.service.manager;

import com.example.model.player.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static final PlayerManager INSTANCE = new PlayerManager();

    private final Map<String, Player> players = new ConcurrentHashMap<>();

    private PlayerManager() {}

    public PlayerManager getInstance() {
        return INSTANCE;
    }

    /**
     * 创建玩家
     * @return 创建好的玩家
     */
    public Player createPlayer(String name) {
        String playerId;
        do {
            playerId = UUID.randomUUID().toString().substring(0, 8);
        } while (players.containsKey(playerId));
        Player player = new Player(playerId, name);
        players.put(playerId, player);
        System.out.println("Player created: " + playerId);
        return player;
    }

    /**
     * 查找玩家
     */
    public Player getPlayer(String id) {
        return players.get(id);
    }

    /**
     * 移除玩家
     */
    public void removePlayer(String id) {
        players.remove(id);
        System.out.println("Player removed: " + id);
    }
}
