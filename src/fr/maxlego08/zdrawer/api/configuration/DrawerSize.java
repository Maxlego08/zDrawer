package fr.maxlego08.zdrawer.api.configuration;

import fr.maxlego08.zdrawer.api.enums.DrawerType;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.HashMap;
import java.util.Map;

public class DrawerSize {

    private final DrawerType drawerType;
    private final Map<BlockFace, DrawerSizeDirection> drawerSizeDirectionMap = new HashMap<>();

    public DrawerSize(DrawerType drawerType, YamlConfiguration configuration, String path) {
        this.drawerType = drawerType;
        for (String blockFaceKey : configuration.getConfigurationSection(path).getKeys(false)) {

            BlockFace blockFace = BlockFace.valueOf(blockFaceKey.toUpperCase());
            this.drawerSizeDirectionMap.put(blockFace, new DrawerSizeDirection(configuration, path + blockFaceKey + "."));
        }
    }

    public DrawerType getDrawerType() {
        return drawerType;
    }

    public Map<BlockFace, DrawerSizeDirection> getDrawerSizeDirectionMap() {
        return drawerSizeDirectionMap;
    }

    @Override
    public String toString() {
        return "DrawerSize{" +
                "drawerType=" + drawerType +
                ", drawerSizeDirectionMap=" + drawerSizeDirectionMap +
                '}';
    }
}
