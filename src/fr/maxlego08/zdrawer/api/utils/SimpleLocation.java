package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

public class SimpleLocation {

    private final double x;
    private final double y;
    private final double z;
    private final float yaw;
    private final float pitch;

    public SimpleLocation(double x, double y, double z, float yaw, float pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public SimpleLocation(YamlConfiguration configuration, String path) {
        this.x = configuration.getDouble(path + "x");
        this.y = configuration.getDouble(path + "y");
        this.z = configuration.getDouble(path + "z");
        this.yaw = (float) configuration.getDouble(path + "yaw");
        this.pitch = (float) configuration.getDouble(path + "pitch");
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public float getYaw() {
        return yaw;
    }

    public float getPitch() {
        return pitch;
    }

    public void apply(Location location) {
        location.add(x, y, z);
        location.setYaw(yaw);
        location.setPitch(pitch);
    }
}
