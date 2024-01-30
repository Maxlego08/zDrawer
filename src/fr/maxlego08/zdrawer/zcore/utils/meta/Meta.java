package fr.maxlego08.zdrawer.zcore.utils.meta;

import fr.maxlego08.zdrawer.api.utils.TextUpdater;

public class Meta {

    public static TextUpdater meta;

    static {
        try {
            Class.forName("net.kyori.adventure.text.minimessage.MiniMessage");
            meta = new ComponentText();
        } catch (Exception ignored) {
            meta = new ClassicText();
        }
    }

}
