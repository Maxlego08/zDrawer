package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * The SimpleLocation class represents a simplified location in a Minecraft world.
 * It encapsulates coordinates (x, y, z) and orientation (yaw, pitch) parameters.
 */
public class SimpleLocation {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    /**
     * Constructs a new SimpleLocation with specific coordinates and orientation.
     *
     * @param x The x-coordinate of the location.
     * @param y The y-coordinate of the location.
     * @param z The z-coordinate of the location.
     * @param yaw The yaw (horizontal rotation) value of the location.
     * @param pitch The pitch (vertical rotation) value of the location.
     */
    public SimpleLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Constructs a new SimpleLocation by reading values from a YamlConfiguration.
     *
     * @param configuration The YamlConfiguration from which to load the location data.
     * @param path The path prefix used to locate the location settings within the configuration.
     */
    public SimpleLocation(YamlConfiguration configuration, String path) {
        this.x = configuration.getDouble(path + "x");
        this.y = configuration.getDouble(path + "y");
        this.z = configuration.getDouble(path + "z");
        this.yaw = (float) configuration.getDouble(path + "yaw");
        this.pitch = (float) configuration.getDouble(path + "pitch");
    }

    /**
     * Retrieves the x-coordinate of the location.
     *
     * @return The x-coordinate.
     */
    public double getX() {
        return x;
    }

    /**
     * Retrieves the y-coordinate of the location.
     *
     * @return The y-coordinate.
     */
    public double getY() {
        return y;
    }

    /**
     * Retrieves the z-coordinate of the location.
     *
     * @return The z-coordinate.
     */
    public double getZ() {
        return z;
    }

    /**
     * Retrieves the yaw (horizontal rotation) of the location.
     *
     * @return The yaw value.
     */
    public float getYaw() {
        return yaw;
    }

    /**
     * Retrieves the pitch (vertical rotation) of the location.
     *
     * @return The pitch value.
     */
    public float getPitch() {
        return pitch;
    }

    /**
     * Applies this SimpleLocation's coordinates and orientation to a given Bukkit Location.
     *
     * @param location The Bukkit Location to which the SimpleLocation's data is applied.
     * @return The modified Bukkit Location.
     */
    public Location apply(Location location) {
        location.add(x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);
        return location;
    }

    /**
     * Provides a string representation of the SimpleLocation.
     *
     * @return A string detailing the coordinates and orientation of the location.
     */
    @Override
    public String toString() {
        return "SimpleLocation{" +
                "x=" + x +
                ", y=" + y +
                ", z=" + z +
                ", yaw=" + yaw +
                ", pitch=" + pitch +
                '}';
    }
}
