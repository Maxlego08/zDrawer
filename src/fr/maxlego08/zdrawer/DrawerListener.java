package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerCase;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.configuration.DrawerConfiguration;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.api.storage.DrawerContainer;
import fr.maxlego08.zdrawer.api.storage.IStorage;
import fr.maxlego08.zdrawer.api.utils.NamespaceContainer;
import fr.maxlego08.zdrawer.listener.ListenerAdapter;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.zcore.logger.Logger;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Barrel;
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
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.RayTraceResult;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

public class DrawerListener extends ListenerAdapter {

    private final DrawerPlugin plugin;
    private final DrawerManager manager;

    public DrawerListener(DrawerPlugin plugin) {
        this.plugin = plugin;
        this.manager = plugin.getManager();
    }

    @Override
    protected void onInteract(PlayerInteractEvent event, Player player) {

        System.out.println("A " + event.isCancelled());
        System.out.println("B " + event.useInteractedBlock());
        System.out.println("C " + event.useItemInHand());
        if (event.isCancelled()) return;

        Block block = event.getClickedBlock();
        if (block == null) return;

        Optional<Drawer> optional = this.manager.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();

        // The player will type another face than the one in front, so he wants to perform another action, like placing a block. We stop the code here.
        if (event.getBlockFace().getOppositeFace() != drawer.getBlockFace()) return;

        event.setCancelled(true);

        ItemStack itemStack = event.getItem();

        if (itemStack != null && (Config.breakMaterials.contains(itemStack.getType()) || !Config.enableBreakMaterial) && event.getAction() == Action.LEFT_CLICK_BLOCK) {
            return;
        }

        if (itemStack != null && Config.blacklistMaterials.contains(itemStack.getType())) return;

        // Check for upgrade
        if (itemStack != null && itemStack.hasItemMeta()) {
            ItemMeta itemMeta = itemStack.getItemMeta();
            PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
            if (persistentDataContainer.has(this.manager.getNamespaceContainer().getDataKeyUpgrade())) {
                String drawerUpgradeName = persistentDataContainer.get(this.manager.getNamespaceContainer().getDataKeyUpgrade(), PersistentDataType.STRING);
                this.manager.getUpgrade(drawerUpgradeName).ifPresent(drawerUpgrade -> {

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

        RayTraceResult result = player.rayTraceBlocks(Config.maxDistance);
        if (result == null) return;

        Location position = result.getHitPosition().toLocation(player.getWorld());

        if (action == Action.RIGHT_CLICK_BLOCK) {

            if (itemStack == null) return;

            drawer.addItem(player, itemStack, event.getHand(), position);
        } else if (action == Action.LEFT_CLICK_BLOCK) {

            drawer.removeItem(player, position);
        }
    }

    @Override
    protected void onBlockBreak(BlockBreakEvent event, Player player) {
        if (event.isCancelled()) return;

        Block block = event.getBlock();
        Optional<Drawer> optional = this.manager.getStorage().getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();
        DrawerConfiguration drawerConfiguration = drawer.getConfiguration();
        if (drawerConfiguration.isDropContent()) {
            if (drawer.getTotalAmount() > drawerConfiguration.getDropLimit()) {
                event.setCancelled(true);
                message(this.plugin, player, Message.BREAK_LIMIT);
                return;
            }
        }

        PlayerInventory inventory = event.getPlayer().getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();

        if ((Config.breakMaterials.contains(itemInMainHand.getType()) || Config.breakMaterials.contains(itemInOffHand.getType())) || !Config.enableBreakMaterial) {

            event.setCancelled(false);
            event.setDropItems(false);
            event.setExpToDrop(0);

            drawer.onDisable();
            this.manager.getStorage().removeDrawer(block.getLocation());

            this.manager.getCurrentPlayerDrawer().put(player.getUniqueId(), drawer);

            ItemStack itemStack = this.manager.buildDrawer(drawerConfiguration, player, drawer, false);
            block.getWorld().dropItem(block.getLocation().add(0.5, 0.1, 0.5), itemStack);

            if (drawerConfiguration.isDropContent()) {
                drawer.dropContent(block.getLocation().add(0.5, 0.1, 0.5));
            }
        } else {

            event.setCancelled(true);
        }
    }

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block.getType() != Material.BARREL) return;

        ItemStack itemStack = event.getItemInHand();
        if (!itemStack.hasItemMeta() || !itemStack.getItemMeta().getPersistentDataContainer().has(this.manager.getNamespaceContainer().getDataKeyDrawer()))
            return;

        if (Config.disableWorlds.contains(block.getWorld().getName())) {
            event.setCancelled(true);
            message(this.plugin, player, Message.DISABLE_WORLD);
            return;
        }

        Barrel barrel = (Barrel) block.getBlockData();
        BlockFace blockFace = barrel.getFacing().getOppositeFace();
        Location location = block.getLocation().clone();

        ItemMeta itemMeta = itemStack.getItemMeta();
        NamespaceContainer namespaceContainer = this.manager.getNamespaceContainer();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        String drawerName = persistentDataContainer.get(namespaceContainer.getDataKeyDrawer(), PersistentDataType.STRING);
        Optional<DrawerConfiguration> optional = this.manager.getDrawer(drawerName);
        if (!optional.isPresent()) {
            Logger.info("Impossible to place a drawer, configuration " + drawerName + " doesn't exit !", Logger.LogType.ERROR);
            return;
        }

        Drawer drawer = new ZDrawer(plugin, optional.get(), location, blockFace);

        if (persistentDataContainer.has(namespaceContainer.getDataKeyContent())) {

            String drawerData = persistentDataContainer.getOrDefault(namespaceContainer.getDataKeyContent(), PersistentDataType.STRING, "null");
            drawer.load(drawerData);

            if (persistentDataContainer.has(namespaceContainer.getDataKeyUpgrade())) {
                manager.getUpgrade(persistentDataContainer.get(namespaceContainer.getDataKeyUpgrade(), PersistentDataType.STRING)).ifPresent(drawer::setUpgrade);
            }
        }

        this.manager.getStorage().storeDrawer(drawer);
    }

    @Override
    protected void onQuit(PlayerQuitEvent event, Player player) {
        this.manager.getCurrentPlayerDrawer().remove(player.getUniqueId());
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
        blocks.removeIf(block -> this.manager.getStorage().getDrawer(block.getLocation()).isPresent());
    }

    @Override
    public void onInventoryMove(InventoryMoveItemEvent event, Inventory destination, ItemStack item, Inventory source, Inventory initiator) {

        if (event.isCancelled()) return;

        IStorage storage = this.manager.getStorage();
        if (source.getHolder() instanceof org.bukkit.block.Barrel) {

            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) source.getHolder();
            Optional<Drawer> optional = storage.getDrawer(barrel.getLocation());
            if (!optional.isPresent()) return;
            Drawer drawer = optional.get();
            event.setCancelled(true);

            if (drawer.getConfiguration().isDisableHopper()) return;

            Optional<DrawerCase> optionalDrawerCase = drawer.findFirstCase();
            optionalDrawerCase.ifPresent(drawerCase -> {
                if (drawerCase.getAmount() <= 0) return;

                ItemStack newItemStack = drawerCase.getItemStack().clone();
                newItemStack.setAmount(1);
                destination.addItem(newItemStack);

                drawerCase.remove(1);
            });
            return;
        }

        if (destination.getHolder() instanceof org.bukkit.block.Barrel) {

            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) destination.getHolder();
            Optional<Drawer> optional = storage.getDrawer(barrel.getLocation());
            if (!optional.isPresent()) return;
            Drawer drawer = optional.get();

            if (drawer.getConfiguration().isDisableHopper()) {
                event.setCancelled(true);
                return;
            }

            ItemStack newItemStack = item.clone();
            Optional<DrawerCase> optionalDrawerCase = drawer.findDrawerCase(newItemStack);

            if (!optionalDrawerCase.isPresent()) {
                event.setCancelled(true);
                return;
            }

            DrawerCase drawerCase = optionalDrawerCase.get();

            if (!drawerCase.hasItemStack()) {

                drawerCase.setAmount(newItemStack.getAmount());
                drawerCase.setItemStack(newItemStack);
                event.setItem(new ItemStack(Material.AIR));

            } else if (drawerCase.getItemStack().isSimilar(newItemStack)) {

                if (drawerCase.isFull()) {
                    event.setCancelled(true);
                    return;
                }

                int addAmount = (int) Math.min(newItemStack.getAmount(), drawer.getLimit() - drawerCase.getAmount());
                drawerCase.add(addAmount);
                event.setItem(new ItemStack(Material.AIR));

            } else {
                event.setCancelled(true);
            }
        }
    }

    @Override
    protected void onPrepareItemCraft(PrepareItemCraftEvent event, Recipe recipe) {
        event.getViewers().stream().findFirst().ifPresent(viewer -> {
            if (viewer instanceof Player) {

                if (recipe == null) return;

                ItemStack itemStack = recipe.getResult();
                if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(this.manager.getNamespaceContainer().getDataKeyDrawer())) {

                    String drawerName = itemStack.getItemMeta().getPersistentDataContainer().get(this.manager.getNamespaceContainer().getDataKeyDrawer(), PersistentDataType.STRING);
                    this.manager.getDrawer(drawerName).ifPresent(drawerConfiguration -> {

                        // Drawer of a new drawer, so we will remove the cache
                        this.manager.getCurrentPlayerDrawer().remove(viewer.getUniqueId());
                        event.getInventory().setItem(0, this.manager.buildDrawer(drawerConfiguration, (Player) viewer, null, false));
                    });
                }
            }
        });
    }

    @Override
    public void onInventoryOpen(InventoryOpenEvent event, Player player, Inventory inventory) {
        if (inventory.getHolder() instanceof org.bukkit.block.Barrel) {
            org.bukkit.block.Barrel barrel = (org.bukkit.block.Barrel) inventory.getHolder();
            this.manager.getStorage().getDrawer(barrel.getLocation()).ifPresent(drawer -> {
                event.setCancelled(true);
            });
        }
    }

    @Override
    public void onUnLoad(ChunkUnloadEvent event, Chunk chunk) {
        this.manager.getStorage().getDrawers(chunk).forEach(Drawer::onDisable);
    }

    @Override
    public void onLoad(ChunkLoadEvent event, Chunk chunk) {
        clearChunk(this.plugin, chunk);
        this.manager.getStorage().getDrawers(chunk).forEach(Drawer::onLoad);
    }

    @Override
    protected void onWorldLoad(WorldLoadEvent event, World world) {

        clearWorld(this.plugin, world);

        IStorage storage = this.manager.getStorage();
        List<DrawerContainer> drawerContainers = storage.getWaitingWorldDrawers();
        String worldName = world.getName();
        Iterator<DrawerContainer> iterator = drawerContainers.iterator();
        while (iterator.hasNext()) {
            DrawerContainer drawerContainer = iterator.next();
            if (drawerContainer.getWorldName().equals(worldName)) {
                iterator.remove();
                storage.createDrawer(drawerContainer);
            }
        }
    }
}
