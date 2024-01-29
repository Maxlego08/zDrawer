package fr.maxlego08.zdrawer.zcore.utils;

import it.unimi.dsi.fastutil.ints.Int2IntSortedMaps;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.TextDisplay;

import java.lang.reflect.Method;

public class TextDisplayUtil {

    /**
     * Sets the displayed text on a TextDisplay object using the appropriate method.
     * It tries to use the non-deprecated 'text(Component)' method if available,
     * otherwise falls back to the deprecated 'setText(String)' method.
     *
     * @param textDisplay The TextDisplay object on which to set the text.
     * @param text        The text to set.
     */
    public static void setDisplayedText(TextDisplay textDisplay, String text) {
        try {
            // Check if the non-deprecated method exists
            Method textMethod = TextDisplay.class.getMethod("text", Component.class);
            // If it exists, use it
            textMethod.invoke(textDisplay, Component.text(text));
        } catch (NoSuchMethodException exception) {
            // If the non-deprecated method doesn't exist, use the deprecated one
            textDisplay.setText(text);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}