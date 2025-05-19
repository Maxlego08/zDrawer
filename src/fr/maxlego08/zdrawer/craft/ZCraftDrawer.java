package fr.maxlego08.zdrawer.craft;

import fr.maxlego08.zdrawer.DrawerPlugin;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;

public class ZCraftDrawer extends ZCraft {

    public ZCraftDrawer(DrawerPlugin plugin, String path, YamlConfiguration configuration, String name, File file) {
        super(plugin, path, configuration, name, file);
    }

    @Override
    public ItemStack getResultItemStack(Player player) {
        ItemStack itemStack = super.getResultItemStack(player);

        ItemMeta itemMeta = itemStack.getItemMeta();
        PersistentDataContainer persistentDataContainer = itemMeta.getPersistentDataContainer();
        persistentDataContainer.set(this.plugin.getManager().getNamespaceContainer().getDataKeyDrawer(), PersistentDataType.STRING, this.getName());
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }
}
