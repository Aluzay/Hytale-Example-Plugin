package com.aluzay.shop.listeners;

import com.aluzay.shop.ShopPlugin;
import com.aluzay.shop.models.PlayerData;
import com.aluzay.shop.services.PlayerService;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.entity.entities.Player;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;

import java.util.UUID;

public class PlayerJoinListener {

    public void onPlayerJoin(PlayerReadyEvent event) {
        Player player = event.getPlayer();
        UUID playerUUID = PlayerData.getUUIDofPlayer(player);
        PlayerService playerService = ShopPlugin.getInstance().getPlayerService();

        Message welcomeMessage = Message.raw("Welcome to the server, " + player.getDisplayName() + "!");
        player.sendMessage(welcomeMessage);

        if (playerService.getPlayer(playerUUID) == null) {
            playerService.createPlayerData(playerUUID, player.getDisplayName());
        }
    }
}
