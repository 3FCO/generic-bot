package me.efco;

public class BotUtilities {
    public static int parseIntegerOrDefault(String intString, int defaultValue) {
        int returningValue = defaultValue;
        try {
            returningValue = Integer.parseInt(intString);
        } catch (NumberFormatException e) { }

        return returningValue;
    }
}
