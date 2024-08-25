package fr.maxlego08.zdrawer.hook;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerAccess;
import fr.maxlego08.zdrawer.api.events.DrawerBlockBreakEvent;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public class DefaultBlockAccess implements DrawerAccess {
    @Override
    public boolean hasAccess(Player player, Location location, Drawer drawer) {
        Block block = location.getBlock();
        DrawerBlockBreakEvent event = new DrawerBlockBreakEvent(block, player);
        return event.callEvent();
    }
}
