package fr.maxlego08.zdrawer.save;

import fr.maxlego08.zdrawer.zcore.utils.storage.Persist;
import fr.maxlego08.zdrawer.api.storage.Savable;
import org.bukkit.Material;

import java.util.Arrays;
import java.util.List;

public class Config implements Savable {

    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;

    public static List<Material> breakMaterials = Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
    public static List<Material> blacklistMaterials = Arrays.asList(Material.BARREL, Material.BEDROCK);

    /**
     * static Singleton instance.
     */
    private static volatile Config instance;


    /**
     * Private constructor for singleton.
     */
    private Config() {
    }

    /**
     * Return a singleton instance of Config.
     */
    public static Config getInstance() {
        // Double lock for thread safety.
        if (instance == null) {
            synchronized (Config.class) {
                if (instance == null) {
                    instance = new Config();
                }
            }
        }
        return instance;
    }

    public void save(Persist persist) {
    }

    public void load(Persist persist) {
    }

}
