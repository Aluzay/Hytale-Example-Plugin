package com.aluzay.shop.services;

import com.aluzay.shop.models.PlayerData;
import com.aluzay.shop.storage.PlayerStorage;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerService {
    private final PlayerStorage playerStorage;
    private final Map<UUID, PlayerData> playersCache;

    public PlayerService(PlayerStorage playerStorage) {
        this.playerStorage = playerStorage;
        this.playersCache = new ConcurrentHashMap<>();
        loadAll();
    }

    private void loadAll() {
        for (PlayerData data : playerStorage.loadAll()) {
            playersCache.put(data.getUuid(), data);
        }
    }

    public PlayerData getPlayer(UUID uuid) {
        return playersCache.computeIfAbsent(uuid, playerStorage::load);
    }

    public void createPlayerData(UUID uuid, String username) {
        PlayerData playerData = new PlayerData(uuid, username, 0.0);
        playerStorage.save(playerData);
        playersCache.put(uuid, playerData);
    }

    public void savePlayer(PlayerData playerData) {
        playersCache.put(playerData.getUuid(), playerData);
        playerStorage.save(playerData);
    }

    public void saveAll() {
        for (PlayerData data : playersCache.values()) {
            playerStorage.save(data);
        }
    }
}
