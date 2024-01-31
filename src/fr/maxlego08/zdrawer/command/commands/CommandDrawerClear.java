package fr.maxlego08.zdrawer.command.commands;

import fr.maxlego08.zdrawer.DrawerPlugin;
import fr.maxlego08.zdrawer.api.enums.Message;
import fr.maxlego08.zdrawer.command.VCommand;
import fr.maxlego08.zdrawer.zcore.enums.Permission;
import fr.maxlego08.zdrawer.zcore.utils.commands.CommandType;
import org.bukkit.World;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.entity.TextDisplay;

import java.util.concurrent.atomic.AtomicLong;

public class CommandDrawerClear extends VCommand {

    public CommandDrawerClear(DrawerPlugin plugin) {
        super(plugin);
        this.setConsoleCanUse(false);
        this.setPermission(Permission.ZDRAWER_CLEAR);
        this.addSubCommand("clear");
        this.setDescription(Message.DESCRIPTION_CLEAR);
    }

    @Override
    protected CommandType perform(DrawerPlugin plugin) {

        World world = this.player.getWorld();
        AtomicLong amount = new AtomicLong();
        world.getEntitiesByClasses(TextDisplay.class, ItemDisplay.class).forEach(display -> {
            if (display.getPersistentDataContainer().has(plugin.getManager().getNamespaceContainer().getDataKeyEntity())) {
                display.remove();
                amount.addAndGet(1);
            }
        });

        message(plugin, this.sender, Message.CLEAR_DISPLAY, "%amount%", amount.get());

        return CommandType.SUCCESS;
    }

}
