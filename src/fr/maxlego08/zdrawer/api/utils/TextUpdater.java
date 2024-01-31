package fr.maxlego08.zdrawer.api.utils;

import org.bukkit.entity.TextDisplay;

/**
 * Interface for updating text displays, with support for MiniMessage formatting if available.
 * <p>
 * This interface defines a method for updating the text of a TextDisplay. If the server supports
 * MiniMessage formatting, the text can include MiniMessage format tags for enhanced styling and formatting.
 * Otherwise, it will use basic Minecraft color and formatting codes. This allows for flexible text
 * updating that adapts to the capabilities of the server.
 * </p>
 */
public interface TextUpdater {

    /**
     * Updates the text of a given TextDisplay.
     * <p>
     * This method takes a TextDisplay object and a text string, then updates the display with the given text.
     * If MiniMessage formatting is supported by the server, the text can include MiniMessage tags.
     * Otherwise, only basic Minecraft formatting codes will be used.
     * </p>
     *
     * @param textDisplay The TextDisplay object to be updated.
     * @param text        The new text to display, possibly including formatting codes.
     */
    void updateText(TextDisplay textDisplay, String text);

}

