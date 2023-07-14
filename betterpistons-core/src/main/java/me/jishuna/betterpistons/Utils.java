package me.jishuna.betterpistons;

public class Utils {
    private static boolean isPaper;
    private static boolean initialized = false;

    public static boolean isPaper() {
        if (!initialized) {
            isPaper = hasClass("com.destroystokyo.paper.PaperConfig") || hasClass("io.papermc.paper.configuration.Configuration");
            initialized = true;
        }
        return isPaper;
    }

    private static boolean hasClass(String className) {
        try {
            Class.forName(className);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
