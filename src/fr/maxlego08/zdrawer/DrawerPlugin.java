package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.storage.DrawerStorage;
import fr.maxlego08.zdrawer.command.commands.CommandDrawer;
import fr.maxlego08.zdrawer.placeholder.LocalPlaceholder;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.save.MessageLoader;
import fr.maxlego08.zdrawer.storage.StorageManager;
import fr.maxlego08.zdrawer.zcore.ZPlugin;
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
    private final DrawerStorage storage = new StorageManager();

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("zdrawer");

        this.preEnable();

        ServicesManager servicesManager = this.getServer().getServicesManager();
        servicesManager.register(DrawerStorage.class, this.storage, this, ServicePriority.High);
        servicesManager.register(DrawerManager.class, this.manager, this, ServicePriority.High);

        this.registerCommand("zdrawer", new CommandDrawer(this));

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));
        this.addSave(this.storage);

        this.addListener(this.manager);

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

    public DrawerStorage getStorage() {
        return storage;
    }

    public DrawerManager getManager() {
        return manager;
    }
}
