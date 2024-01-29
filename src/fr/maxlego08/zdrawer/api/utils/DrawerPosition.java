package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The DrawerPosition class is responsible for managing the position details of various components of a Drawer.
 * It includes positions for item display, upgrade display, text display, and border positions.
 */
public class DrawerPosition {

    private final BlockFace blockFace;
    private final SimpleLocation itemDisplay;
    private final SimpleLocation upgradeDisplay;
    private final SimpleLocation textDisplay;
    private final BorderPositions borderPositions;

    /**
     * Constructs a new DrawerPosition object from a YamlConfiguration, a path prefix, and a BlockFace.
     *
     * @param configuration The YamlConfiguration from which to load the position settings.
     * @param path The path prefix used to locate the position settings within the configuration.
     * @param blockFace The facing of the block which determines the orientation of the drawer.
     */
    public DrawerPosition(YamlConfiguration configuration, String path, BlockFace blockFace) {
        this.blockFace = blockFace;
        this.itemDisplay = new SimpleLocation(configuration, path + "itemDisplay.");
        this.upgradeDisplay = new SimpleLocation(configuration, path + "upgradeDisplay.");
        this.textDisplay = new SimpleLocation(configuration, path + "textDisplay.");
        this.borderPositions = new BorderPositions(configuration, path + "border.");
    }

    /**
     * Retrieves the block face orientation of the drawer.
     *
     * @return The BlockFace indicating the orientation of the drawer.
     */
    public BlockFace getBlockFace() {
        return blockFace;
    }

    /**
     * Retrieves the position of the item display.
     *
     * @return The SimpleLocation of the item display.
     */
    public SimpleLocation getItemDisplay() {
        return itemDisplay;
    }

    /**
     * Retrieves the position of the upgrade display.
     *
     * @return The SimpleLocation of the upgrade display.
     */
    public SimpleLocation getUpgradeDisplay() {
        return upgradeDisplay;
    }

    /**
     * Retrieves the position of the text display.
     *
     * @return The SimpleLocation of the text display.
     */
    public SimpleLocation getTextDisplay() {
        return textDisplay;
    }

    /**
     * Retrieves the border positions of the drawer.
     *
     * @return The BorderPositions object representing the positions of the drawer's borders.
     */
    public BorderPositions getBorderPositions() {
        return borderPositions;
    }
}
