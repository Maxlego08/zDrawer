package fr.maxlego08.zdrawer.save;

import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import fr.maxlego08.zdrawer.zcore.utils.FormatConfig;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Config {

    public static final List<FormatConfig> formatConfigs = new ArrayList<>();
    public static boolean enableDebug = true;
    public static boolean enableDebugTime = false;
    public static List<Material> breakMaterials = Arrays.asList(Material.WOODEN_AXE, Material.STONE_AXE, Material.IRON_AXE, Material.GOLDEN_AXE, Material.DIAMOND_AXE, Material.NETHERITE_AXE);
    public static List<Material> blacklistMaterials = Arrays.asList(Material.BARREL, Material.BEDROCK);
    public static String defaultFormat = "%amount%";
    public static boolean enableFormatting = false;

    public static DisplaySize itemDisplaySize;
    public static DisplaySize upgradeDisplaySize;
    public static DisplaySize textDisplaySize;
    public static double maxDistance = 4.5;

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

    public void load(YamlConfiguration configuration) {

        enableDebug = configuration.getBoolean("enableDebug");
        enableDebugTime = configuration.getBoolean("enableDebugTime");
        blacklistMaterials = configuration.getStringList("drawer.blacklistMaterials").stream().map(Material::valueOf).collect(Collectors.toList());
        breakMaterials = configuration.getStringList("drawer.breakMaterials").stream().map(Material::valueOf).collect(Collectors.toList());

        itemDisplaySize = new DisplaySize(configuration, "drawer.scales.itemDisplay.");
        upgradeDisplaySize = new DisplaySize(configuration, "drawer.scales.upgradeDisplay.");
        textDisplaySize = new DisplaySize(configuration, "drawer.scales.textDisplay.");

        this.loadNumberFormat(configuration);
    }

    private void loadNumberFormat(YamlConfiguration configuration) {

        enableFormatting = configuration.getBoolean("numberFormat.enable", false);
        defaultFormat = configuration.getString("numberFormat.display", "%amount%");
        formatConfigs.clear();

        List<Map<?, ?>> maps = configuration.getMapList("numberFormat.formats");
        maps.forEach(map -> {
            String format = (String) map.get("format");
            String display = (String) map.getOrDefault("display", null);
            long maxAmount = ((Number) map.get("maxAmount")).longValue();
            formatConfigs.add(new FormatConfig(format, display == null ? "%amount%" : display, maxAmount));
        });
    }
}
