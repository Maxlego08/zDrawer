package fr.maxlego08.zdrawer.placeholder;

import fr.maxlego08.zdrawer.api.Drawer;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;

public class DrawerPlaceholder extends ZUtils {

    public void register(DrawerManager manager) {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();

        // %zdrawer_content_<index>%
        placeholder.register("content_", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                try {
                    int index = Integer.parseInt(string);
                    if (drawer.hasItemStack(index)) {
                        return getItemName(drawer.getItemStack(index));
                    }
                } catch (Exception ignored) {
                }
            }
            return Message.EMPTY_DRAWER.getMessage();
        });

        // %zdrawer_amount_formatted_<index>%
        placeholder.register("amount_formatted_", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                try {
                    int index = Integer.parseInt(string);
                    return manager.numberFormat(drawer.getAmount(index), true);
                } catch (Exception ignored) {
                }
            }
            return "0";
        });

        // %zdrawer_amount_<index>%
        placeholder.register("amount_", (player, string) -> {
            if (manager.getCurrentPlayerDrawer().containsKey(player.getUniqueId())) {
                Drawer drawer = manager.getCurrentPlayerDrawer().get(player.getUniqueId());
                try {
                    int index = Integer.parseInt(string);
                    return String.valueOf(drawer.getAmount(index));
                } catch (Exception ignored) {
                }
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
