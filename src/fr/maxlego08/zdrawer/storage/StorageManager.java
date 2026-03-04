package fr.maxlego08.zdrawer.storage;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.enums.StorageType;
import fr.maxlego08.zdrawer.api.storage.DrawerStorage;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.storage.storages.JsonStorage;
import fr.maxlego08.zdrawer.storage.storages.SqliteStorage;
import fr.maxlego08.zdrawer.zcore.logger.Logger;
import fr.maxlego08.zdrawer.zcore.utils.storage.Persist;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class StorageManager implements DrawerStorage {

    private final DrawerPlugin plugin;

    private final StorageType storageType;
    private final ScheduledExecutorService executorService;
    private final ScheduledFuture<?> scheduledTask;
    private IStorage storage;

    public StorageManager(DrawerPlugin plugin) {
        this.plugin = plugin;
        FileConfiguration configuration = plugin.getConfig();
        StorageType type;
        try {
            type = StorageType.valueOf(configuration.getString("storage", "JSON").toUpperCase());
        } catch (IllegalArgumentException e) {
            Logger.info("Invalid storage type in config, defaulting to JSON", Logger.LogType.WARNING);
            type = StorageType.JSON;
        }
        this.storageType = type;
        long updateInterval = configuration.getLong("updateInterval", 12000);
        switch (this.storageType) {

            case JSON:
                this.storage = new JsonStorage(plugin);
                break;
            case SQLITE:
            case MYSQL:
                this.storage = new SqliteStorage(plugin, true);
                break;
        }

        this.executorService = Executors.newSingleThreadScheduledExecutor();
        this.scheduledTask = this.executorService.scheduleAtFixedRate(this::saveTask, updateInterval, updateInterval, TimeUnit.MILLISECONDS);
    }

    @Override
    public void save(Persist persist) {

        this.scheduledTask.cancel(false);
        this.executorService.shutdown();
        try {
            if (!this.executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                this.executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            this.executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }

        this.storage.save();

        if (this.storageType == StorageType.JSON) {
            persist.save(storage, "drawers");
        }
    }

    @Override
    public void load(Persist persist) {

        if (this.storageType == StorageType.JSON) {
            this.storage = persist.loadOrSaveDefault((JsonStorage) this.storage, JsonStorage.class, "drawers");
            ((JsonStorage) this.storage).setPlugin(this.plugin);
        }

        this.storage.load();
    }

    @Override
    public IStorage getStorage() {
        return this.storage;
    }

    @Override
    public void setStorage(IStorage storage) {
        this.storage = storage;
    }

    public void saveTask() {

        this.storage.update();

        if (this.storageType == StorageType.JSON) {
            Persist persist = this.plugin.getPersist();
            persist.save(storage, "drawers");
        }

    }

    @Override
    public StorageType getStorageType() {
        return this.storageType;
    }
}
