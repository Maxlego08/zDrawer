package fr.maxlego08.zdrawer.api.storage;

import fr.maxlego08.zdrawer.api.Drawer;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.Optional;

/**
 * Interface for storage operations related to drawers within the Minecraft world.
 * It provides methods to retrieve, store, and remove drawers, as well as to load, save,
 * and purge data related to drawers.
 */
public interface IStorage {

    /**
     * Retrieves a drawer at the specified location, if one exists.
     *
     * @param location The location to check for a drawer.
     * @return An Optional containing the Drawer if found, or an empty Optional if no drawer exists at the location.
     */
    Optional<Drawer> getDrawer(Location location);

    /**
     * Stores or updates a drawer in the storage system.
     * This method is used to save the state of a drawer, including its contents and configuration.
     *
     * @param drawer The drawer to be stored or updated.
     */
    void storeDrawer(Drawer drawer);

    /**
     * Removes a drawer from the storage system based on its location.
     * This method is used when a drawer is destroyed or otherwise needs to be removed from persistence.
     *
     * @param location The location of the drawer to remove.
     */
    void removeDrawer(Location location);

    /**
     * Loads drawer data from the storage system.
     * This method is typically called during plugin startup or when initializing the storage system.
     */
    void load();

    /**
     * Saves all modified drawer data to the storage system.
     * This method is typically called periodically and during plugin shutdown to ensure data persistence.
     */
    void save();

    /**
     * Purges all drawer data associated with a specific world from the storage system.
     * This can be used to clean up data for worlds that are no longer in use or when performing a bulk deletion operation.
     *
     * @param world The world for which drawer data should be purged.
     */
    void purge(World world);
}
