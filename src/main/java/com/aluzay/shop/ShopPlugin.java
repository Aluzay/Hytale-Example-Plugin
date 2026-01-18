package com.aluzay.shop;

import com.aluzay.shop.listeners.PlayerJoinListener;
import com.aluzay.shop.services.PlayerDataManager;
import com.example.exampleplugin.ExampleCommand;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import java.io.File;

public class ShopPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    private PlayerDataManager playerDataManager;

    public ShopPlugin(JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        playerDataManager = PlayerDataManager.getInstance(
                getDataDirectory().toAbsolutePath(),
                getLogger()
        );

        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, new PlayerJoinListener()::onPlayerJoin);
    }
}
