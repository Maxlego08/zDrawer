package fr.maxlego08.zdrawer;

import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.storage.DrawerStorage;
import fr.maxlego08.zdrawer.command.commands.CommandDrawer;
import fr.maxlego08.zdrawer.placeholder.LocalPlaceholder;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.save.MessageLoader;
import fr.maxlego08.zdrawer.storage.StorageManager;
import fr.maxlego08.zdrawer.zcore.ZPlugin;
import fr.maxlego08.zdrawer.zcore.utils.plugins.Metrics;
import org.bukkit.NamespacedKey;
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
    private final DrawerStorage storage = new StorageManager(this);
    private InventoryManager inventoryManager;
    private NamespacedKey namespacedKeyCraft;
    private NamespacedKey namespacedKeyUpgrade;
    private boolean isSuccessfullyLoaded = false;

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zdrawer");

        this.preEnable();
        this.saveDefaultConfig();

        this.namespacedKeyCraft = new NamespacedKey(this, "zdrawerCraft");
        this.namespacedKeyUpgrade = new NamespacedKey(this, "zdrawerUpgrade");

        ServicesManager servicesManager = this.getServer().getServicesManager();
        servicesManager.register(DrawerStorage.class, this.storage, this, ServicePriority.High);
        servicesManager.register(DrawerManager.class, this.manager, this, ServicePriority.High);

        this.inventoryManager = getProvider(InventoryManager.class);

        this.registerCommand("zdrawer", new CommandDrawer(this), "drawer");

        this.addListener(new DrawerListener(this));

        this.addSave(this.manager);
        this.addSave(new MessageLoader(this));
        this.addSave(this.storage);

        this.loadFiles();

        new Metrics(this, 20791);

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

    public NamespacedKey getNamespacedKeyCraft() {
        return namespacedKeyCraft;
    }

    public NamespacedKey getNamespacedKeyUpgrade() {
        return namespacedKeyUpgrade;
    }
}
