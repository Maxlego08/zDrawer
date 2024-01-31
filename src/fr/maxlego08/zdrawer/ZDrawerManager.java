package fr.maxlego08.zdrawer;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.configuration.DrawerSize;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.enums.DrawerType;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import fr.maxlego08.zdrawer.api.utils.DrawerPosition;
import fr.maxlego08.zdrawer.api.utils.NamespaceContainer;
import fr.maxlego08.zdrawer.craft.ZCraft;
import fr.maxlego08.zdrawer.craft.ZCraftUpgrade;
import fr.maxlego08.zdrawer.placeholder.DrawerPlaceholder;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.zcore.utils.FormatConfig;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import fr.maxlego08.zdrawer.zcore.utils.storage.Persist;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

public class ZDrawerManager extends ZUtils implements DrawerManager {

    private final DrawerPlugin plugin;
    private final NamespaceContainer namespaceContainer;
    private final Map<UUID, Drawer> currentPlayerDrawer = new HashMap<>();
    private final List<Craft> crafts = new ArrayList<>();
    private final List<DrawerUpgrade> drawerUpgrades = new ArrayList<>();
    private final Map<BlockFace, DrawerPosition> drawerPositions = new HashMap<>();
    private final List<DrawerConfiguration> drawerConfigurations = new ArrayList<>();
    private final Map<DrawerType, DrawerSize> drawerSizes = new HashMap<>();

    public ZDrawerManager(DrawerPlugin plugin) {
        this.plugin = plugin;
        this.namespaceContainer = new NamespaceContainer(plugin);

        DrawerPlaceholder drawerPlaceholder = new DrawerPlaceholder();
        drawerPlaceholder.register(this);
    }

    @Override
    public IStorage getStorage() {
        return this.plugin.getStorage().getStorage();
    }

    @Override
    public void save(Persist persist) {
    }

    @Override
    public void load(Persist persist) {

        this.drawerConfigurations.clear();

        InventoryManager inventoryManager = this.plugin.getInventoryManager();
        Loader<MenuItemStack> loader = new MenuItemStackLoader(inventoryManager);
        YamlConfiguration configuration = (YamlConfiguration) plugin.getConfig();

        File file = new File(this.plugin.getDataFolder(), "config.yml");

        try {
            for (String drawerName : configuration.getConfigurationSection("drawer.drawers.").getKeys(false)) {
                String path = "drawer.drawers." + drawerName + ".";
                DrawerConfiguration drawerConfiguration = new DrawerConfiguration(this.plugin, configuration, path, loader, file, drawerName);
                this.drawerConfigurations.add(drawerConfiguration);
            }

            // Load custom crafts
            this.loadCustomCrafts(file, configuration);

            // Load upgrades
            this.loadUpgrades(file, configuration, loader);
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }

        this.drawerSizes.clear();
        for (String drawerTypeKey : configuration.getConfigurationSection("drawer.sizes.").getKeys(false)) {
            DrawerType drawerType = DrawerType.valueOf(drawerTypeKey.toUpperCase());
            DrawerSize size = new DrawerSize(drawerType, configuration, "drawer.sizes." + drawerTypeKey + ".");
            this.drawerSizes.put(drawerType, size);
        }
        this.loadPosition(configuration);

        Config.getInstance().load(configuration);
    }

    private void loadPosition(YamlConfiguration configuration) {

        this.drawerPositions.clear();

        ConfigurationSection section = configuration.getConfigurationSection("drawer.entitiesPosition.");
        if (section == null) return;
        for (String key : section.getKeys(false)) {

            BlockFace blockFace = BlockFace.valueOf(key);
            DrawerPosition drawerPosition = new DrawerPosition(configuration, "drawer.entitiesPosition." + key + ".", blockFace);
            this.drawerPositions.put(blockFace, drawerPosition);
        }
    }

    @Override
    public ItemStack buildDrawer(DrawerConfiguration drawerConfiguration, Player player, Drawer drawer) {
        ItemStack itemStackDrawer = drawerConfiguration.getMenuItemStack().build(player, false);
        ItemMeta itemMeta = itemStackDrawer.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(this.namespaceContainer.getDataKeyDrawer(), PersistentDataType.STRING, drawerConfiguration.getName());

        if (drawer != null) {
            if (player != null) this.currentPlayerDrawer.put(player.getUniqueId(), drawer);

            persistentDataContainer.set(this.namespaceContainer.getDataKeyContent(), PersistentDataType.STRING, drawer.getData());

            if (drawer.getUpgrade() != null) {
                persistentDataContainer.set(this.namespaceContainer.getDataKeyUpgrade(), PersistentDataType.STRING, drawer.getUpgradeName());
            }
        }

        itemStackDrawer.setItemMeta(itemMeta);
        return itemStackDrawer;
    }

    private void loadCustomCrafts(File file, YamlConfiguration configuration) throws InventoryException {

        this.crafts.forEach(Craft::unregister);
        this.crafts.clear();

        for (String craftName : configuration.getConfigurationSection("customCrafts.").getKeys(false)) {
            String path = "customCrafts." + craftName + ".";
            Craft craft = new ZCraft(this.plugin, path, configuration, craftName, file);
            craft.register();
            this.crafts.add(craft);
        }
    }

    private void loadUpgrades(File file, YamlConfiguration configuration, Loader<MenuItemStack> loader) throws InventoryException {

        this.drawerUpgrades.clear();

        for (String upgradeName : configuration.getConfigurationSection("upgrades.").getKeys(false)) {

            String path = "upgrades." + upgradeName + ".";
            Craft craft = new ZCraftUpgrade(this.plugin, path + "craft.", configuration, upgradeName, file);
            this.crafts.add(craft);
            craft.register();

            long limit = configuration.getLong(path + "limit", 0);

            ItemStack displayItemStack = loader.load(configuration, path + "displayItem.", file).build(null);
            String displayName = configuration.getString(path + "displayName");
            DrawerUpgrade drawerUpgrade = new ZDrawerUpgrade(upgradeName, displayName, craft, limit, displayItemStack);
            this.drawerUpgrades.add(drawerUpgrade);
        }
    }

    @Override
    public Optional<Craft> getCraft(String craftName) {
        return this.crafts.stream().filter(craft -> craft.getName().equals(craftName)).findFirst();
    }

    @Override
    public Optional<DrawerUpgrade> getUpgrade(String upgradeName) {
        return this.drawerUpgrades.stream().filter(drawerUpgrade -> drawerUpgrade.getName().equals(upgradeName)).findFirst();
    }

    @Override
    public Optional<DrawerConfiguration> getDrawer(String drawerName) {
        return this.drawerConfigurations.stream().filter(drawerConfiguration -> drawerConfiguration.getName().equalsIgnoreCase(drawerName)).findFirst();
    }

    @Override
    public List<String> getDrawerNames() {
        return this.drawerConfigurations.stream().map(DrawerConfiguration::getName).collect(Collectors.toList());
    }

    @Override
    public void giveDrawer(CommandSender sender, Player player, String drawerName, DrawerUpgrade drawerUpgrade, Material material, Long amount) {

        Optional<DrawerConfiguration> optional = this.getDrawer(drawerName);
        if (!optional.isPresent()) {
            message(this.plugin, sender, Message.DRAWER_NOT_FOUND, "%name%", drawerName);
            return;
        }

        DrawerConfiguration drawerConfiguration = optional.get();
        Drawer drawer = new ZDrawer(this.plugin, material, amount, drawerUpgrade, drawerConfiguration);
        this.currentPlayerDrawer.put(player.getUniqueId(), drawer);

        ItemStack itemStack = buildDrawer(drawerConfiguration, player, drawer);
        give(player, itemStack, false);

        message(this.plugin, sender, Message.DRAWER_GIVE_SENDER, "%player%", player.getName());
        message(this.plugin, player, Message.DRAWER_GIVE_RECEIVE);
    }

    @Override
    public List<String> getUpgradeNames() {
        List<String> names = new ArrayList<>();
        names.add("none");
        names.addAll(this.drawerUpgrades.stream().map(DrawerUpgrade::getName).collect(Collectors.toList()));
        return names;
    }

    @Override
    public List<String> getCraftNames() {
        return this.crafts.stream().map(Craft::getName).collect(Collectors.toList());
    }

    @Override
    public void giveCraft(CommandSender sender, Player player, String craftName) {

        Optional<Craft> optional = getCraft(craftName);
        if (!optional.isPresent()) {
            message(this.plugin, sender, Message.CRAFT_GIVE_ERROR);
            return;
        }

        Craft craft = optional.get();
        ItemStack itemStack = craft.getResultItemStack(player);
        give(player, itemStack, false);

        message(this.plugin, sender, Message.CRAFT_GIVE_SENDER, "%player%", player.getName(), "%name%", craft.getName());
        message(this.plugin, player, Message.CRAFT_GIVE_RECEIVE, "%name%", craft.getName());
    }

    @Override
    public void placeDrawer(CommandSender sender, String drawerName, World world, double x, double y, double z, BlockFace blockFace, DrawerUpgrade drawerUpgrade, Material material, long amount) {

        Location location = new Location(world, x, y, z);
        Optional<Drawer> optional = this.getStorage().getDrawer(location);
        if (optional.isPresent()) {
            message(this.plugin, sender, Message.DRAWER_PLACE_ERROR);
            return;
        }

        Optional<DrawerConfiguration> optionalDrawerConfiguration = this.getDrawer(drawerName);
        if (!optionalDrawerConfiguration.isPresent()) {
            message(this.plugin, sender, Message.DRAWER_NOT_FOUND, "%name%", drawerName);
            return;
        }

        DrawerConfiguration drawerConfiguration = optionalDrawerConfiguration.get();
        Drawer drawer = new ZDrawer(this.plugin, drawerConfiguration, location, blockFace);

        if (drawerUpgrade != null) {
            drawer.setUpgrade(drawerUpgrade);
        }

        for (int index = 0; index < drawerConfiguration.getDrawerType().getSize(); index++) {
            drawer.setAmount(index, amount);
            drawer.setItemStack(index, new ItemStack(material));
        }

        this.getStorage().storeDrawer(drawer);

        message(this.plugin, sender, Message.DRAWER_PLACE_SUCCESS, "%world%", world.getName(), "%x%", format(x), "%y%", format(y), "%z%", format(z));
    }

    @Override
    public void purgeWorld(CommandSender sender, World world, boolean destroyBlock) {

        getStorage().purge(world, destroyBlock);
        message(this.plugin, sender, Message.DRAWER_PURGE, "%world%", world.getName());

    }

    @Override
    public String numberFormat(long number, boolean force) {

        if (!Config.enableFormatting && !force) return Config.defaultFormat.replace("%amount%", String.valueOf(number));

        for (FormatConfig config : Config.formatConfigs) {
            if (number < config.getMaxAmount()) {

                String displayText = config.getDisplay();
                if (config.getFormat().isEmpty()) {
                    return displayText.replace("%amount%", String.valueOf(number));
                }

                double divisor = config.getMaxAmount() == 1000 ? 1000.0 : config.getMaxAmount() / 1000.0;
                return displayText.replace("%amount%", String.format(config.getFormat(), number / divisor));
            }
        }
        return String.valueOf(number);
    }

    @Override
    public DrawerPosition getDrawerPosition(BlockFace blockFace) {
        return this.drawerPositions.get(blockFace);
    }

    @Override
    public DisplaySize getItemDisplaySize() {
        return Config.itemDisplaySize;
    }

    @Override
    public DisplaySize getUpgradeDisplaySize() {
        return Config.upgradeDisplaySize;
    }

    @Override
    public DisplaySize getTextDisplaySize() {
        return Config.textDisplaySize;
    }

    @Override
    public Map<UUID, Drawer> getCurrentPlayerDrawer() {
        return this.currentPlayerDrawer;
    }

    @Override
    public NamespaceContainer getNamespaceContainer() {
        return this.namespaceContainer;
    }

    @Override
    public DrawerSize getSize(DrawerType drawerType) {
        return this.drawerSizes.get(drawerType);
    }
}
