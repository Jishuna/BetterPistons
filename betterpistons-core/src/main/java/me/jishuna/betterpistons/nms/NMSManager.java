package me.jishuna.betterpistons.nms;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class NMSManager {
    private static final String PACKAGE = "me.jishuna.betterpistons.nms.";

    private static NMSAdapter adapter;

    public static void initAdapater(Plugin plugin) {
        String version = getServerVersion();

        try {
            adapter = (NMSAdapter) Class.forName(PACKAGE + version + ".NMSAdapter").getDeclaredConstructor().newInstance();
            plugin.getLogger().log(Level.INFO, "Supported server version detected: {0}", version);
        } catch (ReflectiveOperationException e) {
            plugin.getLogger().log(Level.SEVERE, "Server version \"{0}\" is unsupported! Please check for updates!", version);
            Bukkit.getPluginManager().disablePlugin(plugin);
        }
    }

    public static NMSAdapter getAdapter() {
        return adapter;
    }

    private static String getServerVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

}