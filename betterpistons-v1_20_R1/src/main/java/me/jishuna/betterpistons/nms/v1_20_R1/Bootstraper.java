package me.jishuna.betterpistons.nms.v1_20_R1;

import java.lang.invoke.ConstantCallSite;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class Bootstraper {
    private static ClassLoader loader;

    public static ConstantCallSite bootstrap(final MethodHandles.Lookup lookup, final String targetMethodName, final MethodType targetMethodType, String targetClass) {
        try {
            if (loader == null) {
                findLoader(lookup.lookupClass().getClassLoader());
            }

            return new ConstantCallSite(lookup.findStatic(Class.forName(targetClass, true, loader), targetMethodName, targetMethodType));
        } catch (ReflectiveOperationException e) {
            e.printStackTrace();
        }
        return new ConstantCallSite(null);
    }

    private static void findLoader(ClassLoader source) {
        try {
            Class<?> bukkitClass = Class.forName("org.bukkit.Bukkit", true, source);
            Object pluginManager = bukkitClass.getDeclaredMethod("getPluginManager").invoke(null);
            Object plugin = pluginManager.getClass().getDeclaredMethod("getPlugin", String.class).invoke(pluginManager, "BetterPistons");
            loader = plugin.getClass().getClassLoader();
        } catch (ReflectiveOperationException ex) {
            ex.printStackTrace();
        }
    }
}
