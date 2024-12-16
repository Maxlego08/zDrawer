package fr.maxlego08.zdrawer.api.storage;

import fr.maxlego08.zdrawer.api.Drawer;
import org.bukkit.Bukkit;
import org.bukkit.block.BlockFace;

/**
 * Represents a container for storing all relevant information about a Drawer.
 * <p>
 * This class encapsulates the data required to fully describe a Drawer's state,
 * including its location, orientation, name, upgrade status, and item data.
 * The data field typically contains detailed information about the items within the Drawer,
 * such as types, quantities, and other item-specific data.
 * This class is primarily used for persisting Drawer information in storage solutions like JSON or databases.
 * </p>
 */
public class DrawerContainer {

    private final String location;
    private final BlockFace blockFace;
    private final String drawerName;
    private String data;
    private final String upgrade;

    /**
     * Constructs a new DrawerContainer with the specified details.
     *
     * @param location   The location of the Drawer, usually in a serialized format.
     * @param blockFace  The orientation of the Drawer as a {@link BlockFace}.
     * @param drawerName The name of the Drawer.
     * @param data       Serialized data representing the contents and state of the Drawer.
     * @param upgrade    Information about any upgrades applied to the Drawer.
     */
    public DrawerContainer(String location, BlockFace blockFace, String drawerName, String data, String upgrade) {
        this.location = location;
        this.blockFace = blockFace;
        this.drawerName = drawerName;
        this.data = data;
        this.upgrade = upgrade;
    }

    // JavaDoc for each getter method
    public String getLocation() {
        return location;
    }

    public BlockFace getBlockFace() {
        return blockFace;
    }

    public String getDrawerName() {
        return drawerName;
    }

    public String getData() {
        return data;
    }

    public String getUpgrade() {
        return upgrade;
    }

    /**
     * Checks if the world where the Drawer is located is currently loaded.
     *
     * @return true if the world is loaded, false otherwise.
     */
    public boolean isWorldLoaded() {
        return Bukkit.getWorld(getWorldName()) != null;
    }

    /**
     * Extracts and returns the name of the world from the stored location.
     *
     * @return The name of the world as a String.
     */
    public String getWorldName() {
        String[] locationArray = this.location.split(",");
        return locationArray[0];
    }

    /**
     * Checks if this DrawerContainer has upgrade information.
     *
     * @return true if there is upgrade information, false otherwise.
     */
    public boolean hasUpgrade() {
        return this.upgrade != null;
    }

    /**
     * Checks if this DrawerContainer contains data about the items in the Drawer.
     *
     * @return true if there is item data, false otherwise.
     */
    public boolean hasData() {
        return this.data != null;
    }


    public void setData(String data) {
        this.data = data;
    }
}
