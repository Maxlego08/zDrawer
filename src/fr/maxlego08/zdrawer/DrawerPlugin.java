package fr.maxlego08.zdrawer;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.api.scheduler.ZScheduler;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.storage.DrawerStorage;
import fr.maxlego08.zdrawer.command.commands.CommandDrawer;
import fr.maxlego08.zdrawer.hook.WorldGuardAccess;
import fr.maxlego08.zdrawer.save.MessageLoader;
import fr.maxlego08.zdrawer.storage.StorageManager;
import fr.maxlego08.zdrawer.zcore.ZPlugin;
import fr.maxlego08.zdrawer.zcore.logger.Logger;
import fr.maxlego08.zdrawer.zcore.utils.nms.NmsVersion;
import fr.maxlego08.zdrawer.zcore.utils.plugins.Metrics;
import fr.maxlego08.zdrawer.zcore.utils.plugins.Plugins;
import fr.maxlego08.zdrawer.zcore.utils.plugins.VersionChecker;
import org.bukkit.Bukkit;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class DrawerPlugin extends ZPlugin {

    private final ZDrawerManager manager = new ZDrawerManager(this);
    private DrawerStorage storage;
    private InventoryManager inventoryManager;
    private boolean isSuccessfullyLoaded = false;

    @Override
    public void onEnable() {

        if (!NmsVersion.getCurrentVersion().isDisplayVersion()) {
            Logger.info("The plugin only works from 1.19 ! We advise you to use the plugin from 1.20", Logger.LogType.ERROR);
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }

        this.preEnable();

        this.saveDefaultConfig();

        ServicesManager servicesManager = this.getServer().getServicesManager();
        servicesManager.register(DrawerStorage.class, this.storage, this, ServicePriority.High);
        servicesManager.register(DrawerManager.class, this.manager, this, ServicePriority.High);

        this.inventoryManager = getProvider(InventoryManager.class);

        this.registerCommand("zdrawer", new CommandDrawer(this), "drawer");

        this.addListener(new DrawerListener(this));
        new VersionChecker(this, 313).useLastVersion();

        this.addSave(this.manager);
        this.addSave(new MessageLoader(this));

        this.storage = new StorageManager(this);
        this.addSave(this.storage);

        this.loadFiles();

        new Metrics(this, 20791);

        if (this.isEnable(Plugins.WORLDGUARD)) {
            this.manager.addAccess(new WorldGuardAccess());
        }

        this.postEnable();

        this.isSuccessfullyLoaded = true;
    }

    @Override
    public void onDisable() {

        this.preDisable();

        if (this.isSuccessfullyLoaded) {
            this.saveFiles();
        }

        this.postDisable();
    }

    public DrawerStorage getStorage() {
        return storage;
    }

    public DrawerManager getManager() {
        return manager;
    }

    public InventoryManager getInventoryManager() {
        return inventoryManager;
    }

    public ZScheduler getScheduler() {
        return this.inventoryManager.getScheduler();
    }
}
