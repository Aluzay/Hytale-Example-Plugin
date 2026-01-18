package com.aluzay.shop.models;

import com.google.gson.annotations.SerializedName;
import com.hypixel.hytale.component.Ref;
import com.hypixel.hytale.component.Store;
import com.hypixel.hytale.server.core.entity.UUIDComponent;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.universe.world.storage.EntityStore;

import java.util.UUID;

public class PlayerData {
    @SerializedName("uuid")
    private UUID uuid;

    @SerializedName("username")
    private String username;

    @SerializedName("balance")
    private double balance;

    public PlayerData(UUID uuid, String username, double balance) {
        this.uuid = uuid;
        this.username = username;
        this.balance = balance;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public static UUID getUUIDofPlayer(Player player) {
        Ref<EntityStore> ref = player.getReference();

        if (ref != null) {
            Store<EntityStore> store = ref.getStore();

            UUIDComponent uuidComponent = store.getComponent(ref, UUIDComponent.getComponentType());

            if (uuidComponent != null) {
                return uuidComponent.getUuid();
            }
        }

        return null;
    }
}
