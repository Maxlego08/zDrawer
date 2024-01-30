package fr.maxlego08.zdrawer.zcore.utils;

/**
 * The FormatConfig class represents a configuration for formatting some aspect of the application.
 * It holds a format pattern, a display string, and a maximum amount value, which its used
 * for formatting and display drawer amount.
 */
public class FormatConfig {
    private final String format;
    private final String display;
    private final long maxAmount;

    /**
     * Constructs a new FormatConfig with a specified format, display string, and maximum amount.
     *
     * @param format    The format pattern to be used.
     * @param display   The string to be used for display purposes.
     * @param maxAmount The maximum amount or limit associated with this configuration.
     */
    public FormatConfig(String format, String display, long maxAmount) {
        this.format = format;
        this.display = display;
        this.maxAmount = maxAmount;
    }

    /**
     * Retrieves the format pattern of this configuration.
     *
     * @return String The format pattern.
     */
    public String getFormat() {
        return format;
    }

    /**
     * Retrieves the display string of this configuration.
     *
     * @return String The display string.
     */
    public String getDisplay() {
        return display;
    }

    /**
     * Retrieves the maximum amount or limit of this configuration.
     *
     * @return long The maximum amount.
     */
    public long getMaxAmount() {
        return maxAmount;
    }

    public static class Result {
        private final String result;
        private final String displayText;

        public Result(String result, String displayText) {
            this.result = result;
            this.displayText = displayText;
        }

        public String getResult() {
            return result;
        }

        public String getDisplayText() {
            return displayText;
        }
    }

}
