package com.aluzay.shop.listeners;

import com.aluzay.shop.models.PlayerData;
import com.aluzay.shop.services.PlayerDataManager;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

import java.util.UUID;

public class PlayerJoinListener {

    public void onPlayerJoin(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = PlayerData.getUUIDofPlayer(player);

        Message welcomeMessage = Message.raw("Welcome to the server, " + player.getDisplayName() + "!");
        player.sendMessage(welcomeMessage);

        if (!PlayerDataManager.getInstance().hasPlayerData(playerUUID)) {
            PlayerDataManager.getInstance().createPlayerData(playerUUID, player.getDisplayName());
        }
    }
}
