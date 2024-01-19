package fr.maxlego08.zdrawer;

import fr.maxlego08.zdrawer.command.commands.CommandTemplate;
import fr.maxlego08.zdrawer.placeholder.LocalPlaceholder;
import fr.maxlego08.zdrawer.save.Config;
import fr.maxlego08.zdrawer.save.MessageLoader;
import fr.maxlego08.zdrawer.zcore.ZPlugin;

/**
 * System to create your plugins very simply Projet:
 * <a href="https://github.com/Maxlego08/TemplatePlugin">https://github.com/Maxlego08/TemplatePlugin</a>
 *
 * @author Maxlego08
 */
public class DrawerPlugin extends ZPlugin {

    @Override
    public void onEnable() {

        LocalPlaceholder placeholder = LocalPlaceholder.getInstance();
        placeholder.setPrefix("template");

        this.preEnable();

        this.registerCommand("template", new CommandTemplate(this));

        this.addSave(Config.getInstance());
        this.addSave(new MessageLoader(this));

        this.addListener(new TestManager());

        this.loadFiles();

        this.postEnable();
    }

    @Override
    public void onDisable() {

        this.preDisable();

        this.saveFiles();

        this.postDisable();
    }

}
