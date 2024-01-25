package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Represents the size of a display element in three dimensions: x, y, and z.
 * This class is commonly used to define the size of items, upgrades, or text displays associated with drawers
 */
public class DisplaySize {

    private final double x;
    private final double y;
    private final double z;

    /**
     * Constructs a new DisplaySize with specified dimensions.
     *
     * @param x The size along the x-axis.
     * @param y The size along the y-axis.
     * @param z The size along the z-axis.
     */
    public DisplaySize(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Constructs a new DisplaySize by reading dimensions from a YamlConfiguration.
     *
     * @param configuration The YamlConfiguration from which to read the size.
     * @param path The base path in the configuration where the size is defined.
     */
    public DisplaySize(YamlConfiguration configuration, String path) {
        this(configuration.getDouble(path + ".x"), configuration.getDouble(path + ".y"), configuration.getDouble(path + ".z"));
    }

    /**
     * Gets the size along the x-axis.
     *
     * @return The x dimension of the display size.
     */
    public double getX() {
        return x;
    }

    /**
     * Gets the size along the y-axis.
     *
     * @return The y dimension of the display size.
     */
    public double getY() {
        return y;
    }

    /**
     * Gets the size along the z-axis.
     *
     * @return The z dimension of the display size.
     */
    public double getZ() {
        return z;
    }
}
