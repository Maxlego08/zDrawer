package fr.maxlego08.zdrawer.api.storage;

import fr.maxlego08.zdrawer.api.Drawer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

public interface IStorage {

    Optional<Drawer> getDrawer(Location location);

    void storeDrawer(Drawer drawer);

    void removeDrawer(Location location);

    void load();

    void save();

    void purge(World world);
}
