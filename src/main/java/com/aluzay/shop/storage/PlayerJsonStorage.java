package com.aluzay.shop.storage;

import com.aluzay.shop.models.PlayerData;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PlayerJsonStorage implements PlayerStorage {
    private final File dataFolder;

    public PlayerJsonStorage(File dataFolder) {
        this.dataFolder = new File(dataFolder, "players");
        if (!this.dataFolder.exists()) {
            this.dataFolder.mkdirs();
        }
    }

    @Override
    public PlayerData load(UUID uuid) {
        File file = new File(dataFolder, uuid.toString() + ".json");

        if (!file.exists()) {
            return null; // No player found
        }

        try (var reader = new java.io.FileReader(file)) {
            return new Gson().fromJson(reader, PlayerData.class);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void save(PlayerData data) {
        File file = new File(dataFolder, data.getUuid().toString() + ".json");

        try (FileWriter writer = new FileWriter(file)) {
            new Gson().toJson(data, writer);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<PlayerData> loadAll() {
        List<PlayerData> players = new ArrayList<>();
        File[] files = dataFolder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files != null) {
            for (File file : files) {
                try (var reader = new java.io.FileReader(file)) {
                    PlayerData data = new Gson().fromJson(reader, PlayerData.class);
                    players.add(data);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return players;
    }
}
