package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

/**
 * The NamespaceContainer class is used to manage different NamespacedKeys
 * for zDrawer. These keys are used to uniquely identify various data elements
 * within the plugin's data storage, such as drawer contents, item stacks, craft data,
 * upgrades, etc.
 */
public class NamespaceContainer {

    private final NamespacedKey dataKeyDrawer;
    private final NamespacedKey dataKeyCraft;
    private final NamespacedKey dataKeyItemstack;
    private final NamespacedKey dataKeyUpgrade;
    private final NamespacedKey dataKeyAmount;

    /**
     * Constructs a new NamespaceContainer with specific namespaced keys for each data type.
     *
     * @param plugin The plugin instance for which the namespaced keys are being created.
     */
    public NamespaceContainer(Plugin plugin) {
        this.dataKeyDrawer = new NamespacedKey(plugin, "content");
        this.dataKeyItemstack = new NamespacedKey(plugin, "itemstack");
        this.dataKeyAmount = new NamespacedKey(plugin, "amount");
        this.dataKeyCraft = new NamespacedKey(plugin, "craft");
        this.dataKeyUpgrade = new NamespacedKey(plugin, "upgrade");
    }

    /**
     * Retrieves the namespaced key for drawer data.
     *
     * @return NamespacedKey The namespaced key for drawer data.
     */
    public NamespacedKey getDataKeyDrawer() {
        return dataKeyDrawer;
    }

    /**
     * Retrieves the namespaced key for craft data.
     *
     * @return NamespacedKey The namespaced key for craft data.
     */
    public NamespacedKey getDataKeyCraft() {
        return dataKeyCraft;
    }

    /**
     * Retrieves the namespaced key for item stack data.
     *
     * @return NamespacedKey The namespaced key for item stack data.
     */
    public NamespacedKey getDataKeyItemstack() {
        return dataKeyItemstack;
    }

    /**
     * Retrieves the namespaced key for upgrade data.
     *
     * @return NamespacedKey The namespaced key for upgrade data.
     */
    public NamespacedKey getDataKeyUpgrade() {
        return dataKeyUpgrade;
    }

    /**
     * Retrieves the namespaced key for amount data.
     *
     * @return NamespacedKey The namespaced key for amount data.
     */
    public NamespacedKey getDataKeyAmount() {
        return dataKeyAmount;
    }
}
