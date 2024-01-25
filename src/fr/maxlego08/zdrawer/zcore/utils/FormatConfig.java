package fr.maxlego08.zdrawer.zcore.utils;

public class FormatConfig {
    private final String format;
    private final long maxAmount;

    public FormatConfig(String format, long maxAmount) {
        this.format = format;
        this.maxAmount = maxAmount;
    }

    public String getFormat() {
        return format;
    }

    public long getMaxAmount() {
        return maxAmount;
    }

    @Override
    public String toString() {
        return "FormatConfig{" +
                "format='" + format + '\'' +
                ", maxAmount=" + maxAmount +
                '}';
    }
}