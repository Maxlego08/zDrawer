package fr.maxlego08.zdrawer.api.configuration;

import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class DrawerSizeDirection {

    private final DisplaySize scale;
    private final List<DrawerSizePosition> positions = new ArrayList<>();

    public DrawerSizeDirection(YamlConfiguration configuration, String path) {
        this.scale = new DisplaySize(configuration, path + "scale.");
        for (String key : configuration.getConfigurationSection(path + "positions.").getKeys(false)) {
            positions.add(new DrawerSizePosition(configuration, path + "positions." + key + "."));
        }
    }

    public DisplaySize getScale() {
        return scale;
    }

    public List<DrawerSizePosition> getPositions() {
        return positions;
    }

    @Override
    public String toString() {
        return "DrawerSizeDirection{" + "scale=" + scale + ", positions=" + positions + '}';
    }

    public static class DrawerSizePosition {

        private final DisplaySize itemSize;
        private final DisplaySize textSize;

        public DrawerSizePosition(YamlConfiguration configuration, String path) {
            this.itemSize = new DisplaySize(configuration, path + "item.");
            this.textSize = new DisplaySize(configuration, path + "text.");
        }

        public DisplaySize getItemSize() {
            return itemSize;
        }

        public DisplaySize getTextSize() {
            return textSize;
        }
    }
}