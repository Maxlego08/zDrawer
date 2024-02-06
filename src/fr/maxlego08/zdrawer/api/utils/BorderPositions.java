package fr.maxlego08.zdrawer.api.utils;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.configuration.DrawerBorder;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Display;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;

import java.util.List;

/**
 * The BorderPositions class is responsible for handling the positions of the borders of a Drawer.
 * It allows for creating, storing, and manipulating the positions for up, down, left, and right sides of a Drawer.
 */
public class BorderPositions {

    private final SimpleLocation up;
    private final SimpleLocation down;
    private final SimpleLocation left;
    private final SimpleLocation right;

    /**
     * Constructs a new BorderPositions object from a YamlConfiguration and a path prefix.
     *
     * @param configuration The YamlConfiguration from which to load the border positions.
     * @param path          The path prefix used to locate the border position settings within the configuration.
     */
    public BorderPositions(YamlConfiguration configuration, String path) {
        this.up = new SimpleLocation(configuration, path + "up.");
        this.down = new SimpleLocation(configuration, path + "down.");
        this.left = new SimpleLocation(configuration, path + "left.");
        this.right = new SimpleLocation(configuration, path + "right.");
    }

    /**
     * Retrieves the upper border position.
     *
     * @return The SimpleLocation of the upper border.
     */
    public SimpleLocation getUp() {
        return up;
    }

    /**
     * Retrieves the lower border position.
     *
     * @return The SimpleLocation of the lower border.
     */
    public SimpleLocation getDown() {
        return down;
    }

    /**
     * Retrieves the left border position.
     *
     * @return The SimpleLocation of the left border.
     */
    public SimpleLocation getLeft() {
        return left;
    }

    /**
     * Retrieves the right border position.
     *
     * @return The SimpleLocation of the right border.
     */
    public SimpleLocation getRight() {
        return right;
    }

    @Override
    public String toString() {
        return "BorderPositions{" +
                "up=" + up +
                ", down=" + down +
                ", left=" + left +
                ", right=" + right +
                '}';
    }

    /**
     * Spawns item displays at each border position using the provided plugin, drawer, and drawer border settings.
     *
     * @param plugin       The plugin instance used for creating metadata values.
     * @param drawer       The Drawer instance for which the borders are being set.
     * @param drawerBorder The DrawerBorder containing the settings and item stack for border display.
     */
    public void spawn(DrawerPlugin plugin, Drawer drawer, DrawerBorder drawerBorder) {

        ItemStack itemStack = drawerBorder.getMenuItemStack().build(null);
        Location location = drawer.getLocation().clone();

        Location locationUp = this.up.apply(location.clone());
        Location locationDown = this.down.apply(location.clone());
        Location locationLeft = this.left.apply(location.clone());
        Location locationRight = this.right.apply(location.clone());

        List<ItemDisplay> itemDisplays = drawer.getBorderDisplays();
        itemDisplays.add(spawn(plugin, locationUp, itemStack, drawerBorder.getUpScale()));
        itemDisplays.add(spawn(plugin, locationDown, itemStack, drawerBorder.getDownScale()));
        itemDisplays.add(spawn(plugin, locationLeft, itemStack, drawerBorder.getLeftScale()));
        itemDisplays.add(spawn(plugin, locationRight, itemStack, drawerBorder.getRightScale()));
    }

    /**
     * Helper method to spawn an individual ItemDisplay at a specified location.
     *
     * @param plugin    The plugin instance used for creating metadata values.
     * @param location  The location at which to spawn the ItemDisplay.
     * @param itemStack The ItemStack to be displayed.
     * @param scale     The display size of the item.
     * @return The spawned ItemDisplay.
     */
    private ItemDisplay spawn(DrawerPlugin plugin, Location location, ItemStack itemStack, DisplaySize scale) {
        World world = location.getWorld();

        return world.spawn(location, ItemDisplay.class, display -> {

            display.setItemStack(itemStack);
            Transformation transformation = display.getTransformation();
            transformation.getScale().set(scale.getX(), scale.getY(), scale.getZ());
            display.setBillboard(Display.Billboard.FIXED);
            display.setInvulnerable(true);
            display.setTransformation(transformation);
            display.getPersistentDataContainer().set(plugin.getManager().getNamespaceContainer().getDataKeyEntity(), PersistentDataType.BOOLEAN, true);
            display.setMetadata("zdrawer", new FixedMetadataValue(plugin, true));
        });
    }
}
