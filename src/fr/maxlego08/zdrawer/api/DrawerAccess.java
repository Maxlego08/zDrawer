package fr.maxlego08.zdrawer.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

public interface DrawerAccess {

    boolean hasAccess(Player player, Location location, Drawer drawer);

}
