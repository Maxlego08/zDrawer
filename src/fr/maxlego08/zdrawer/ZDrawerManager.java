package fr.maxlego08.zdrawer;

import fr.maxlego08.menu.MenuItemStack;
import fr.maxlego08.menu.api.InventoryManager;
import fr.maxlego08.menu.exceptions.InventoryException;
import fr.maxlego08.menu.loader.MenuItemStackLoader;
import fr.maxlego08.menu.zcore.utils.loader.Loader;
import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.craft.Craft;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.api.utils.DisplaySize;
import fr.maxlego08.zdrawer.api.utils.DrawerPosition;
import fr.maxlego08.zdrawer.craft.ZCraft;
import fr.maxlego08.zdrawer.craft.ZCraftUpgrade;
import fr.maxlego08.zdrawer.listener.ListenerAdapter;
import fr.maxlego08.zdrawer.placeholder.LocalPlaceholder;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.zcore.utils.FormatConfig;
import fr.maxlego08.zdrawer.zcore.utils.nms.ItemStackUtils;
import fr.maxlego08.zdrawer.zcore.utils.storage.Persist;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
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

public class ZDrawerManager extends ListenerAdapter implements DrawerManager {

    private final DrawerPlugin plugin;
    private final NamespacedKey DATA_KEY_DRAWER;
    private final NamespacedKey DATA_KEY_CRAFT;
    private final NamespacedKey DATA_KEY_ITEMSTACK;
    private final NamespacedKey DATA_KEY_UPGRADE;
    private final NamespacedKey DATA_KEY_AMOUNT;
    private final Map<UUID, Drawer> currentPlayerDrawer = new HashMap<>();
    private final List<Craft> crafts = new ArrayList<>();
    private final List<DrawerUpgrade> drawerUpgrades = new ArrayList<>();
    private final List<FormatConfig> formatConfigs = new ArrayList<>();
    private final Map<BlockFace, DrawerPosition> drawerPositions = new HashMap<>();
    private MenuItemStack drawerItemStack;
    private Map<String, MenuItemStack> ingredients = new HashMap<>();
    private List<String> shade;
    private long drawerLimit;
    private boolean enableFormatting = false;
    private DisplaySize itemDisplaySize;
    private DisplaySize upgradeDisplaySize;
    private DisplaySize textDisplaySize;

    public ZDrawerManager(DrawerPlugin plugin) {
        this.plugin = plugin;
        this.DATA_KEY_DRAWER = new NamespacedKey(plugin, "zdrawerContent");
        this.DATA_KEY_ITEMSTACK = new NamespacedKey(plugin, "zdrawerItemstack");
        this.DATA_KEY_AMOUNT = new NamespacedKey(plugin, "zdrawerAmount");
        this.DATA_KEY_CRAFT = new NamespacedKey(this.plugin, "zdrawerCraft");
        this.DATA_KEY_UPGRADE = new NamespacedKey(this.plugin, "zdrawerUpgradeName");

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.register("content", (player, string) -> {
            if (this.currentPlayerDrawer.containsKey(player.getUniqueId())) {
                Drawer drawer = this.currentPlayerDrawer.get(player.getUniqueId());
                if (drawer.hasItemStack()) {
                    return getItemName(drawer.getItemStack());
                }
            }
            return Message.EMPTY_DRAWER.getMessage();
        });
        placeholder.register("amount", (player, string) -> {
            if (this.currentPlayerDrawer.containsKey(player.getUniqueId())) {
                Drawer drawer = this.currentPlayerDrawer.get(player.getUniqueId());
                return String.valueOf(drawer.getAmount());
            }
            return "0";
        });
        placeholder.register("upgrade", (player, string) -> {
            if (this.currentPlayerDrawer.containsKey(player.getUniqueId())) {
                Drawer drawer = this.currentPlayerDrawer.get(player.getUniqueId());
                DrawerUpgrade drawerUpgrade = drawer.getUpgrade();
                if (drawerUpgrade != null) {

                }
            }
            return Message.EMPTY_UPGRADE.getMessage();
        });
    }

    @Override
    protected void onQuit(PlayerQuitEvent event, Player player) {
        this.currentPlayerDrawer.remove(player.getUniqueId());
    }

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block.getType() != Material.BARREL) return;

        ItemStack itemStack = event.getItemInHand();
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(this.DATA_KEY_DRAWER))
            return;

        Barrel barrel = (Barrel) block.getBlockData();
        BlockFace blockFace = barrel.getFacing().getOppositeFace();
        Location location = block.getLocation().clone();

        Drawer drawer = new ZDrawer(plugin, location, blockFace);
        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();

        if (persistentDataContainer.has(DATA_KEY_AMOUNT) && persistentDataContainer.has(DATA_KEY_ITEMSTACK)) {
            String itemStackAsString = persistentDataContainer.getOrDefault(DATA_KEY_ITEMSTACK, PersistentDataType.STRING, "null");
            long amount = persistentDataContainer.getOrDefault(DATA_KEY_AMOUNT, PersistentDataType.LONG, 0L);
            ItemStack drawerItemStack = ItemStackUtils.deserializeItemStack(itemStackAsString);
            if (drawerItemStack != null) {
                drawer.setItemStack(drawerItemStack);
                drawer.setAmount(amount);
            }

            if (persistentDataContainer.has(DATA_KEY_UPGRADE)) {
                getUpgrade(persistentDataContainer.get(DATA_KEY_UPGRADE, PersistentDataType.STRING)).ifPresent(drawer::setUpgrade);
            }
        }

        this.getStorage().storeDrawer(drawer);
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event, Player player, Inventory inventory) {
        if (inventory.getHolder() instanceof org.bukkit.block.Barrel) {
            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) inventory.getHolder();
            this.getStorage().getDrawer(barrel.getLocation()).ifPresent(drawer -> {
                // event.setCancelled(true);
                System.out.println("ON PEUT OPEN C NORMAL ON TEST");
            });
        }
    }

    @Override
    protected void onInteract(PlayerInteractEvent event, Player player) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Optional<Drawer> optional = this.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();

        // The player will type another face than the one in front, so he wants to perform another action, like placing a block. We stop the code here.
        if (event.getBlockFace().getOppositeFace() != drawer.getBlockFace()) return;

        event.setCancelled(true);

        ItemStack itemStack = event.getItem();

        if (itemStack != null && Config.breakMaterials.contains(itemStack.getType()) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (itemStack != null && Config.blacklistMaterials.contains(itemStack.getType())) return;

        // Check for upgrade
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            if (persistentDataContainer.has(this.plugin.getNamespacedKeyUpgrade())) {
                String drawerUpgradeName = persistentDataContainer.get(this.plugin.getNamespacedKeyUpgrade(), PersistentDataType.STRING);
                this.getUpgrade(drawerUpgradeName).ifPresent(drawerUpgrade -> {

                    if (drawer.getLimit() >= drawerUpgrade.getLimit()) {
                        message(this.plugin, player, Message.UPGRADE_ERROR_LIMIT);
                        return;
                    }

                    drawer.setUpgrade(drawerUpgrade);

                    if (itemStack.getAmount() > 1) itemStack.setAmount(itemStack.getAmount() - 1);
                    else player.getInventory().setItem(event.getHand(), new ItemStack(Material.AIR));

                    message(this.plugin, player, Message.UPGRADE_SUCCESS, "%name%", drawerUpgrade.getDisplayName());
                });
                return;
            }

        }

        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {

            if (itemStack == null) return;

            drawer.addItem(player, itemStack, event.getHand());
        } else if (action == Action.LEFT_CLICK_BLOCK) {

            drawer.removeItem(player);
        }
    }

    @Override
    protected void onBlockBreak(BlockBreakEvent event, Player player) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        Optional<Drawer> optional = this.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();
        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();

        if (Config.breakMaterials.contains(itemInMainHand.getType()) || Config.breakMaterials.contains(itemInOffHand.getType())) {

            event.setCancelled(false);
            event.setDropItems(false);
            event.setExpToDrop(0);

            drawer.onDisable();
            getStorage().removeDrawer(block.getLocation());

            this.currentPlayerDrawer.put(player.getUniqueId(), drawer);

            ItemStack itemStack = buildDrawer(player, drawer);
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.1, 0.5), itemStack);
        } else {

            event.setCancelled(true);
        }
    }

    private IStorage getStorage() {
        return this.plugin.getStorage().getStorage();
    }

    @Override
    public void save(Persist persist) {
    }

    @Override
    public void load(Persist persist) {

        InventoryManager inventoryManager = this.plugin.getInventoryManager();
        Loader<MenuItemStack> loader = new MenuItemStackLoader(inventoryManager);
        YamlConfiguration configuration = (YamlConfiguration) plugin.getConfig();

        this.drawerLimit = configuration.getLong("drawer.limit", 0);

        File file = new File(this.plugin.getDataFolder(), "config.yml");
        try {
            this.drawerItemStack = loader.load(configuration, "drawer.item.", file);
            loadCraft(configuration, loader, file);

            // Load custom crafts
            this.loadCustomCrafts(file, configuration);

            // Load upgrades
            this.loadUpgrades(file, configuration, loader);
        } catch (InventoryException exception) {
            exception.printStackTrace();
        }

        ItemStack resultItemStack = this.buildDrawer(null, null);

        ShapedRecipe recipe = new ShapedRecipe(this.DATA_KEY_CRAFT, resultItemStack);
        recipe.shape(this.shade.toArray(new String[0]));

        ingredients.forEach((identifier, ingredient) -> {
            recipe.setIngredient(identifier.charAt(0), new RecipeChoice.ExactChoice(ingredient.build(null)));
        });

        Server server = this.plugin.getServer();
        server.removeRecipe(this.DATA_KEY_CRAFT, false);
        server.addRecipe(recipe);
        server.updateRecipes();

        this.loadNumberFormat(configuration);
        this.loadPosition(configuration);

        this.itemDisplaySize = new DisplaySize(configuration, "drawer.sizes.itemDisplay.");
        this.upgradeDisplaySize = new DisplaySize(configuration, "drawer.sizes.upgradeDisplay.");
        this.textDisplaySize = new DisplaySize(configuration, "drawer.sizes.textDisplay.");

        Config.enableDebug = configuration.getBoolean("enableDebug");
        Config.enableDebugTime = configuration.getBoolean("enableDebugTime");
        Config.blacklistMaterials = configuration.getStringList("drawer.blacklistMaterials").stream().map(Material::valueOf).collect(Collectors.toList());
        Config.breakMaterials = configuration.getStringList("drawer.breakMaterials").stream().map(Material::valueOf).collect(Collectors.toList());
    }

    private void loadNumberFormat(YamlConfiguration configuration) {

        enableFormatting = configuration.getBoolean("numberFormat.enable", false);
        this.formatConfigs.clear();

        List<Map<?, ?>> maps = configuration.getMapList("numberFormat.formats");
        maps.forEach(map -> {
            String format = (String) map.get("format");
            long maxAmount = ((Number) map.get("maxAmount")).longValue();
            this.formatConfigs.add(new FormatConfig(format, maxAmount));
        });
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

    private void loadCraft(YamlConfiguration configuration, Loader<MenuItemStack> loader, File file) throws InventoryException {

        this.shade = configuration.getStringList("drawer.craft.shade");
        this.ingredients = new HashMap<>();
        for (String ingredientKey : configuration.getConfigurationSection("drawer.craft.ingredients.").getKeys(false)) {
            this.ingredients.put(ingredientKey, loader.load(configuration, "drawer.craft.ingredients." + ingredientKey + ".", file));
        }
    }

    @Override
    protected void onPrepareItemCraft(PrepareItemCraftEvent event, Recipe recipe) {
        event.getViewers().stream().findFirst().ifPresent(viewer -> {
            if (viewer instanceof Player) {
                if (recipe == null) {
                    // handleCustomCraft(event, event.getInventory(), (Player) viewer);
                    return;
                }
                ItemStack itemStack = recipe.getResult();
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(this.DATA_KEY_DRAWER)) {
                    this.currentPlayerDrawer.remove(viewer.getUniqueId());
                    event.getInventory().setItem(0, buildDrawer((Player) viewer, null));
                }
            }
        });
    }

    @Override
    public void onExplode(EntityExplodeEvent event, List<Block> blocks, Entity entity) {
        onExplode(blocks);
    }

    @Override
    public void onExplode(BlockExplodeEvent event, List<Block> blocks, Block block) {
        onExplode(blocks);
    }

    private void onExplode(List<Block> blocks) {
        blocks.removeIf(block -> getStorage().getDrawer(block.getLocation()).isPresent());
    }

    @Override
    public void onInventoryMove(InventoryMoveItemEvent event, Inventory destination, ItemStack item, Inventory source, Inventory initiator) {

        if (event.isCancelled()) return;

        IStorage storage = this.getStorage();
        // Si la source est un barrel, alors l'item qui va partir vert un autre inventaire
        if (source.getHolder() instanceof org.bukkit.block.Barrel) {

            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) source.getHolder();
            Optional<Drawer> optional = storage.getDrawer(barrel.getLocation());
            if (!optional.isPresent()) return;
            Drawer drawer = optional.get();

            event.setCancelled(true);

            if (drawer.getAmount() <= 0) return;

            ItemStack newItemStack = drawer.getItemStack().clone();
            newItemStack.setAmount(1);
            destination.addItem(newItemStack);

            drawer.remove(1);

            return;
        }

        // Si la destination est un barrel, alors l'item va dans le drawer
        if (destination.getHolder() instanceof org.bukkit.block.Barrel) {

            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) destination.getHolder();
            Optional<Drawer> optional = storage.getDrawer(barrel.getLocation());
            if (!optional.isPresent()) return;
            Drawer drawer = optional.get();

            ItemStack newItemStack = item.clone();

            // Si le drawer n'a aucun item, alors on va ajouter l'item du hopper
            if (!drawer.hasItemStack()) {

                drawer.setAmount(newItemStack.getAmount());
                drawer.setItemStack(newItemStack);
                event.setItem(new ItemStack(Material.AIR));

            } else if (drawer.getItemStack().isSimilar(newItemStack)) {

                if (drawer.isFull()) {
                    event.setCancelled(true);
                    return;
                }

                int addAmount = (int) Math.min(newItemStack.getAmount(), drawer.getLimit() - drawer.getAmount());
                drawer.add(addAmount);
                event.setItem(new ItemStack(Material.AIR));

            } else {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public ItemStack buildDrawer(Player player, Drawer drawer) {
        ItemStack itemStackDrawer = this.drawerItemStack.build(player, false);
        ItemMeta itemMeta = itemStackDrawer.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(DATA_KEY_DRAWER, PersistentDataType.BOOLEAN, true);

        if (drawer != null) {
            if (player != null) this.currentPlayerDrawer.put(player.getUniqueId(), drawer);

            persistentDataContainer.set(DATA_KEY_ITEMSTACK, PersistentDataType.STRING, drawer.hasItemStack() ? drawer.getItemStackAsString() : "null");
            persistentDataContainer.set(DATA_KEY_AMOUNT, PersistentDataType.LONG, drawer.getAmount());
            if (drawer.getUpgrade() != null) {
                persistentDataContainer.set(DATA_KEY_UPGRADE, PersistentDataType.STRING, drawer.getUpgradeName());
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
    public long getDrawerLimit() {
        return drawerLimit;
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
    public void giveDrawer(CommandSender sender, Player player, DrawerUpgrade drawerUpgrade, Material material, Long amount) {

        Drawer drawer = new ZDrawer(material, amount, drawerUpgrade);
        this.currentPlayerDrawer.put(player.getUniqueId(), drawer);

        ItemStack itemStack = buildDrawer(player, drawer);
        give(player, itemStack);

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
        give(player, itemStack);

        message(this.plugin, sender, Message.CRAFT_GIVE_SENDER, "%player%", player.getName(), "%name%", craft.getName());
        message(this.plugin, player, Message.CRAFT_GIVE_RECEIVE, "%name%", craft.getName());
    }

    @Override
    public void placeDrawer(CommandSender sender, World world, double x, double y, double z, BlockFace blockFace, DrawerUpgrade drawerUpgrade, Material material, long amount) {

        Location location = new Location(world, x, y, z);
        Optional<Drawer> optional = this.getStorage().getDrawer(location);
        if (optional.isPresent()) {
            message(this.plugin, sender, Message.DRAWER_PLACE_ERROR);
            return;
        }

        Drawer drawer = new ZDrawer(this.plugin, location, blockFace);

        if (drawerUpgrade != null) {
            drawer.setUpgrade(drawerUpgrade);
        }

        if (material != null) {
            drawer.setAmount(amount);
            drawer.setItemStack(new ItemStack(material));
        }

        this.getStorage().storeDrawer(drawer);

        message(this.plugin, sender, Message.DRAWER_PLACE_SUCCESS, "%world%", world.getName(), "%x%", format(x), "%y%", format(y), "%z%", format(z));
    }

    @Override
    public void purgeWorld(CommandSender sender, World world) {

        getStorage().purge(world);
        message(this.plugin, sender, Message.DRAWER_PURGE, "%world%", world.getName());

    }

    @Override
    public String numberFormat(long number) {

        if (!this.enableFormatting) return String.valueOf(number);

        for (FormatConfig config : this.formatConfigs) {
            if (number < config.getMaxAmount()) {
                if (config.getFormat().isEmpty()) return String.valueOf(number);

                double divisor = config.getMaxAmount() == 1000 ? 1000.0 : config.getMaxAmount() / 1000.0;
                return String.format(config.getFormat(), number / divisor);
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
        return this.itemDisplaySize;
    }

    @Override
    public DisplaySize getUpgradeDisplaySize() {
        return this.upgradeDisplaySize;
    }

    @Override
    public DisplaySize getTextDisplaySize() {
        return this.textDisplaySize;
    }
}
