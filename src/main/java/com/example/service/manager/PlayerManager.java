package com.example.service.manager;

import com.example.model.player.Player;
import com.example.service.server.ClientHandler;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static final PlayerManager INSTANCE = new PlayerManager();

    private final Map<String, Player> players = new ConcurrentHashMap<>();
    private final Map<String, ClientHandler> handlerMap = new ConcurrentHashMap<>();

    private PlayerManager() {}

    public static PlayerManager getInstance() {
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
     * 记录 ClientHandler
     */
    public void registerClientHandler(Player player, ClientHandler clientHandler) {
        handlerMap.put(player.getId(), clientHandler);
    }

    /**
     * 查找玩家
     */
    public Player getPlayer(String id) {
        return players.get(id);
    }

    /**
     * 获取玩家对应的 ClientHandler
     */
    public ClientHandler getHandler(String id) {
        return handlerMap.get(id);
    }

    /**
     * 移除玩家
     */
    public void removePlayer(String id) {
        players.remove(id);
        handlerMap.remove(id);
        System.out.println("Player removed: " + id);
    }
}
