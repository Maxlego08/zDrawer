package fr.maxlego08.zdrawer.hook;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerAccess;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

public class DefaultBlockAccess implements DrawerAccess {
    @Override
    public boolean hasAccess(Player player, Location location, Drawer drawer) {
        Block block = location.getBlock();
        BlockBreakEvent event = new BlockBreakEvent(block, player);
        return !event.callEvent();
    }
}
