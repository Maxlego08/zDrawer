package fr.maxlego08.zdrawer.placeholder;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;

public class DrawerPlaceholder extends ZUtils {

    public void register(DrawerManager manager) {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();

        // %zdrawer_content%
        placeholder.register("content", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                /*if (drawer.hasItemStack()) {
                    return getItemName(drawer.getItemStack());
                }*/
                return "TODO - A mettre à jour";
            }
            return Message.EMPTY_DRAWER.getMessage();
        });

        // %zdrawer_amount_formatted%
        placeholder.register("amount_formatted", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                // return manager.numberFormat(drawer.getAmount(), true);
                return "TODO - A mettre à jour";
            }
            return "0";
        });

        // %zdrawer_amount%
        placeholder.register("amount", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                // return String.valueOf(drawer.getAmount());
                return "TODO - A mettre à jour";
            }
            return "0";
        });

        // %zdrawer_upgrade%
        placeholder.register("upgrade", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                DrawerUpgrade drawerUpgrade = drawer.getUpgrade();
                if (drawerUpgrade != null) {
                    return drawerUpgrade.getDisplayName();
                }
            }
            return Message.EMPTY_UPGRADE.getMessage();
        });

    }

}
