package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.listener.ListenerAdapter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.type.Barrel;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TestManager extends ListenerAdapter {

    private final Map<String, Drawer> drawers = new HashMap<>();

    @Override
    protected void onBlockPlace(BlockPlaceEvent event, Player player) {

        if (event.isCancelled()) return;

        Block block = event.getBlock();
        if (block.getType() != Material.BARREL) return;

        Barrel barrel = (Barrel) block.getBlockData();
        BlockFace blockFace = barrel.getFacing().getOppositeFace();
        Location location = block.getLocation().clone();

        Drawer drawer = new Drawer(location, blockFace);
        drawers.put(locationToString(location), drawer);
    }

    @Override
    protected void onInteract(PlayerInteractEvent event, Player player) {

        Block block = event.getClickedBlock();
        if (block == null) return;

        Optional<Drawer> optional = this.getDrawer(block.getLocation());

        if (!optional.isPresent()) return;

        Drawer drawer = optional.get();
        event.setCancelled(true);

        Action action = event.getAction();

        if (action == Action.RIGHT_CLICK_BLOCK) {

            ItemStack itemStack = event.getItem();
            if (itemStack == null) return;

            drawer.addItem(player, itemStack, event.getHand());
        } else if (action == Action.LEFT_CLICK_BLOCK) {

            drawer.removeItem(player);
        }
    }

    private Optional<Drawer> getDrawer(Location location) {
        return Optional.ofNullable(this.drawers.getOrDefault(locationToString(location), null));
    }

    private String locationToString(Location location) {
        return location.getWorld().getName() + "," + location.getBlockX() + "," + location.getBlockY() + "," + location.getBlockZ();
    }
}
