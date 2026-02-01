package com.aluzay.shop.storage;

import com.aluzay.shop.models.PlayerData;

import java.util.List;
import java.util.UUID;

public interface PlayerStorage {
    PlayerData load(UUID uuid);
    void save(PlayerData playerData);
    List<PlayerData> loadAll();
}
