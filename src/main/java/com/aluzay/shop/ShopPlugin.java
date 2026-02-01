package com.aluzay.shop;

import com.aluzay.shop.listeners.PlayerJoinListener;
import com.aluzay.shop.services.PlayerService;
import com.aluzay.shop.storage.PlayerJsonStorage;
import com.example.exampleplugin.ExampleCommand;
import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.event.events.player.PlayerReadyEvent;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ShopPlugin extends JavaPlugin {
    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
    private ScheduledExecutorService threadPool;
    private static ShopPlugin instance;

    public ShopPlugin(JavaPluginInit init) {
        super(init);
        instance = this;

        threadPool = Executors.newScheduledThreadPool(4, runnable -> {
            Thread thread = new Thread(runnable);
            thread.setName("ShopPlugin-Thread" + thread.getId());
            return thread;
        });

        LOGGER.atInfo().log("Hello from %s version %s", this.getName(), this.getManifest().getVersion().toString());
    }

    public static ShopPlugin getInstance() {
        return instance;
    }

    public PlayerService getPlayerService() {
        return playerService;
    }

    @Override
    protected void start() {
        LOGGER.atInfo().log("Plugin enabled");
        // Run task asynchronously
        threadPool.execute(() -> {
            // Heavy operation here
            loadDataFromStorage();
        });

        // Schedule repeating task (every 5 minutes)
        threadPool.scheduleAtFixedRate(
                () -> saveAllData(),
                5, 5, TimeUnit.MINUTES
        );

        // Schedule delayed task (run once after 10 seconds)
        threadPool.schedule(
                () -> cleanupCache(),
                10, TimeUnit.SECONDS
        );
    }

    @Override
    protected void shutdown() {
        threadPool.shutdown();
        try {
            if (!threadPool.awaitTermination(10, TimeUnit.SECONDS)) {
                threadPool.shutdownNow();
            }
        } catch (InterruptedException e) {
            threadPool.shutdownNow();
        }
    }

    @Override
    protected void setup() {
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
        this.getEventRegistry().registerGlobal(PlayerReadyEvent.class, new PlayerJoinListener()::onPlayerJoin);
    }

    private void loadDataFromStorage() {
        playerService = new PlayerService(new PlayerJsonStorage(getDataDirectory().toFile()));
    }

    private void cleanupCache() {
        // Implement cache cleanup logic here
    }

    private void saveAllData() {
        playerService.saveAll();
    }

    ShopPlugin plugin = ShopPlugin.getInstance();
    PlayerService playerService = getPlayerService();
}
