package com.aluzay.shop.services;

import com.aluzay.shop.models.PlayerData;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hypixel.hytale.logger.HytaleLogger;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerDataManager {
    private static PlayerDataManager instance;

    private final Path dataFile;
    private final HytaleLogger logger;
    private final ConcurrentHashMap<UUID, PlayerData> accounts;
    private final Gson gson;

    private PlayerDataManager(Path pluginDirectory, HytaleLogger logger) {
        this.dataFile = pluginDirectory.resolve("data/players.json");
        accounts = new ConcurrentHashMap<>();
        gson = new GsonBuilder().setPrettyPrinting().create();
        this.logger = logger;
        this.logger.atInfo().log("PlayerDataManager initialized. Data file path: %s", dataFile.toString());

        if (!Files.exists(dataFile.getParent())) {
            try {
                Files.createDirectories(dataFile.getParent());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            this.logger.atInfo().log("Data directory already exists: %s", dataFile.getParent().toString());
        }
    }

    public static PlayerDataManager getInstance(Path pluginDirectory, HytaleLogger logger) {
        if (instance == null) {
            instance = new PlayerDataManager(pluginDirectory, logger);
        }
        return instance;
    }

    public static PlayerDataManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("PlayerDataManager is not initialized yet.");
        }
        return instance;
    }

    public void save() {
        try (Writer writer = Files.newBufferedWriter(dataFile, StandardCharsets.UTF_8)) {
            gson.toJson(accounts, writer);
        } catch (IOException e) {
            logger.atSevere().log("Failed to save player data: %s", e.getMessage());
        }
    }

    public void createPlayerData(UUID uuid, String username) {
        PlayerData playerData = new PlayerData(uuid, username, 0.0);
        accounts.put(uuid, playerData);
        logger.atInfo().log("Created new PlayerData for UUID: %s", uuid.toString());
    }

    public boolean hasPlayerData(UUID uuid) {
        return accounts.containsKey(uuid);
    }



    public PlayerData getPlayerData(UUID uuid) {
        return accounts.get(uuid);
    }
}
