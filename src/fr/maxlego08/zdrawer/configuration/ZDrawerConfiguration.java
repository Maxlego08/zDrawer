package fr.maxlego08.zdrawer.configuration;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.configuration.DrawerBorder;
import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.enums.DrawerType;
import fr.maxlego08.zdrawer.craft.ZCraftDrawer;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ZDrawerConfiguration implements DrawerConfiguration {

    private final String name;
    private final DrawerType drawerType;
    private final long limit;
    private final MenuItemStack menuItemStack;
    private final DrawerBorder border;
    private final Craft craft;
    private final boolean enableHopper;
    private final boolean dropItems;
    private final long dropItemLimit;


    public ZDrawerConfiguration(DrawerPlugin plugin, YamlConfiguration configuration, String path, Loader<MenuItemStack> loader, File file, String name) throws InventoryException {
        this.name = name;
        this.drawerType = DrawerType.valueOf(configuration.getString(path + "type", "SIMPLE").toUpperCase());
        this.limit = configuration.getLong(path + "limit", 0);
        this.enableHopper = configuration.getBoolean(path + "enableHopper", true);
        this.dropItems = configuration.getBoolean(path + "dropContent.enable", false);
        this.dropItemLimit = configuration.getLong(path + "dropContent.limit", limit / 2);
        this.menuItemStack = loader.load(configuration, path + "item.", file);
        this.craft = new ZCraftDrawer(plugin, path + "craft.", configuration, name, file);
        this.craft.register();
        this.border = new ZDrawerBorder(configuration, path + "border.", loader, file);
    }

    @Override
    public DrawerType getDrawerType() {
        return drawerType;
    }

    @Override
    public long getLimit() {
        return limit;
    }

    @Override
    public MenuItemStack getMenuItemStack() {
        return menuItemStack;
    }

    @Override
    public DrawerBorder getBorder() {
        return border;
    }

    @Override
    public Craft getCraft() {
        return craft;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isDisableHopper() {
        return !this.enableHopper;
    }

    @Override
    public boolean isDropContent() {
        return this.dropItems;
    }

    @Override
    public long getDropLimit() {
        return this.dropItemLimit;
    }
}
