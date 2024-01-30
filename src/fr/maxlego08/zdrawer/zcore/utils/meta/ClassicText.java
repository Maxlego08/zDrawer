package fr.maxlego08.zdrawer.zcore.utils.meta;

import fr.maxlego08.zdrawer.api.utils.TextUpdater;
import fr.maxlego08.zdrawer.zcore.utils.ZUtils;
import org.bukkit.entity.TextDisplay;

public class ClassicText extends ZUtils implements TextUpdater {
    @Override
    public void updateText(TextDisplay textDisplay, String text) {
        textDisplay.setText(color(text));
    }
}
