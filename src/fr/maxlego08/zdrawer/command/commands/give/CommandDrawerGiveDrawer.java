package fr.maxlego08.zdrawer.command.commands.give;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDrawerGiveDrawer extends VCommand {

    public CommandDrawerGiveDrawer(DrawerPlugin plugin) {
        super(plugin);

        List<String> materials = Arrays.stream(Material.values()).map(material -> material.name().toLowerCase()).collect(Collectors.toList());

        this.addSubCommand("drawer");
        this.setPermission(Permission.ZDRAWER_GIVE_DRAWER);
        this.setDescription(Message.DESCRIPTION_GIVE_DRAWER);
        this.addRequireArg("player");
        this.addRequireArg("drawer", (a, b) -> plugin.getManager().getDrawerNames());
        this.addOptionalArg("upgradeName", (a, b) -> plugin.getManager().getUpgradeNames());
        this.addOptionalArg("material", (a, b) -> materials);
        this.addOptionalArg("amount", (a, b) -> Arrays.asList("1", "2", "4", "6", "8", "16", "32", "64"));
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        DrawerManager manager = plugin.getManager();

        Player player = this.argAsPlayer(0);
        String drawerName = this.argAsString(1);
        String upgradeName = this.argAsString(2);
        String materialName = this.argAsString(3);
        Material material = null;
        try {
            if (materialName != null) material = Material.valueOf(materialName.toUpperCase());
        } catch (Exception ignored) {
        }

        long amount = this.argAsLong(4, 0);
        DrawerUpgrade drawerUpgrade = manager.getUpgrade(upgradeName).orElse(null);

        manager.giveDrawer(this.sender, player, drawerName, drawerUpgrade, material, amount);

        return CommandType.SUCCESS;
    }

}
