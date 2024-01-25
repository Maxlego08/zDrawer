package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;

public class DrawerPosition {

    private final BlockFace blockFace;
    private final SimpleLocation itemDisplay;
    private final SimpleLocation upgradeDisplay;
    private final SimpleLocation textDisplay;

    public DrawerPosition(BlockFace blockFace, SimpleLocation itemDisplay, SimpleLocation upgradeDisplay, SimpleLocation textDisplay) {
        this.blockFace = blockFace;
        this.itemDisplay = itemDisplay;
        this.upgradeDisplay = upgradeDisplay;
        this.textDisplay = textDisplay;
    }

    public DrawerPosition(YamlConfiguration configuration, String path, BlockFace blockFace) {
        this.blockFace = blockFace;
        this.itemDisplay = new SimpleLocation(configuration, path + "itemDisplay.");
        this.upgradeDisplay = new SimpleLocation(configuration, path + "upgradeDisplay.");
        this.textDisplay = new SimpleLocation(configuration, path + "textDisplay.");
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public SimpleLocation getItemDisplay() {
        return itemDisplay;
    }

    public SimpleLocation getUpgradeDisplay() {
        return upgradeDisplay;
    }

    public SimpleLocation getTextDisplay() {
        return textDisplay;
    }
}
