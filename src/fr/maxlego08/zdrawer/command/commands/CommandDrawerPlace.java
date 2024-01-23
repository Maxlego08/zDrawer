package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.DrawerManager;
import fr.maxlego08.zdrawer.api.DrawerUpgrade;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.BlockFace;
import org.bukkit.generator.WorldInfo;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CommandDrawerPlace extends VCommand {

    private final List<BlockFace> blockFaceAllowed = Arrays.asList(
            BlockFace.DOWN,
            BlockFace.UP,
            BlockFace.WEST,
            BlockFace.EAST,
            BlockFace.NORTH,
            BlockFace.SOUTH
    );

    public CommandDrawerPlace(DrawerPlugin plugin) {
        super(plugin);

        List<String> materials = Arrays.stream(Material.values()).map(material -> material.name().toLowerCase()).collect(Collectors.toList());
        List<String> blockFaces = blockFaceAllowed.stream().map(Enum::name).collect(Collectors.toList());

        this.addSubCommand("place");
        this.setPermission(Permission.ZDRAWER_PLACE);
        this.setDescription(Message.DESCRIPTION_PLACE);
        this.addRequireArg("world name", (a, b) -> Bukkit.getWorlds().stream().map(WorldInfo::getName).collect(Collectors.toList()));
        this.addRequireArg("x");
        this.addRequireArg("y");
        this.addRequireArg("z");
        this.addRequireArg("face", (a, b) -> blockFaces);
        this.addOptionalArg("upgradeName", (a, b) -> plugin.getManager().getUpgradeNames());
        this.addOptionalArg("material", (a, b) -> materials);
        this.addOptionalArg("amount", (a, b) -> Arrays.asList("1", "2", "4", "6", "8", "16", "32", "64"));
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        DrawerManager manager = plugin.getManager();

        World world = this.argAsWorld(0);
        double x = this.argAsDouble(1);
        double y = this.argAsDouble(2);
        double z = this.argAsDouble(3);

        BlockFace blockFace = BlockFace.valueOf(this.argAsString(4).toUpperCase());
        if (!blockFaceAllowed.contains(blockFace)) return CommandType.SYNTAX_ERROR;

        String upgradeName = this.argAsString(5);
        String materialName = this.argAsString(6);
        Material material = null;
        try {
            if (materialName != null) material = Material.valueOf(materialName.toUpperCase());
        } catch (Exception ignored) {
        }

        long amount = this.argAsLong(7, 0);
        DrawerUpgrade drawerUpgrade = manager.getUpgrade(upgradeName).orElse(null);

        manager.placeDrawer(this.sender, world, x, y, z, blockFace, drawerUpgrade, material, amount);

        return CommandType.SUCCESS;
    }

}
