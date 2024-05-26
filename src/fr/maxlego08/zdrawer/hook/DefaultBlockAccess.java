package fr.maxlego08.zdrawer.hook;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerAccess;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;

import java.util.ArrayList;
import java.util.Collection;

public class DefaultBlockAccess implements DrawerAccess {

    private final Collection<BlockBreakEvent> accessCheckBlockBreakEvents = new ArrayList<>();

    @Override
    public boolean hasAccess(Player player, Location location, Drawer drawer) {
        BlockBreakEvent event = new BlockBreakEvent(location.getBlock(), player);
        accessCheckBlockBreakEvents.add(event);
        return event.callEvent();
    }

    public boolean isAccessCheckBlockBreakEvent(BlockBreakEvent event) {
        if(accessCheckBlockBreakEvents.contains(event)){
            accessCheckBlockBreakEvents.remove(event);
            return true;
        }

        return false;
    }
}
