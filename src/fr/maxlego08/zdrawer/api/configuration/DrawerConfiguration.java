package fr.maxlego08.zdrawer.api.configuration;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.enums.DrawerType;
import fr.maxlego08.zdrawer.configuration.ZDrawerBorder;
import fr.maxlego08.zdrawer.craft.ZCraftDrawer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class DrawerConfiguration {

    private final String name;
    private final DrawerType drawerType;
    private final long limit;
    private final MenuItemStack menuItemStack;
    private final DrawerBorder border;
    private final Craft craft;

    public DrawerConfiguration(DrawerPlugin plugin, YamlConfiguration configuration, String path, Loader<MenuItemStack> loader, File file, String name) throws InventoryException {
        this.name = name;
        this.drawerType = DrawerType.valueOf(configuration.getString(path + "type", "SIMPLE").toUpperCase());
        this.limit = configuration.getLong(path + "limit", 0);
        this.menuItemStack = loader.load(configuration, path + "item.", file);
        this.craft = new ZCraftDrawer(plugin, path + "craft.", configuration, name, file);
        this.craft.register();
        this.border = new ZDrawerBorder(configuration, path + "border.", loader, file);
    }

    public DrawerType getDrawerType() {
        return drawerType;
    }

    public long getLimit() {
        return limit;
    }

    public MenuItemStack getMenuItemStack() {
        return menuItemStack;
    }

    public DrawerBorder getBorder() {
        return border;
    }

    public Craft getCraft() {
        return craft;
    }

    public String getName() {
        return name;
    }
}
