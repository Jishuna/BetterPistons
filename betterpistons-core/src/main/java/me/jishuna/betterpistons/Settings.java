package me.jishuna.betterpistons;

import java.util.Set;

import org.bukkit.Material;

import me.jishuna.jishlib.config.annotation.Comment;
import me.jishuna.jishlib.config.annotation.ConfigEntry;

public class Settings {
    @ConfigEntry("blacklist")
    @Comment("Blocks in this list will never be able to be moved by pistons.")
    public static Set<Material> BLACKLISTED_MATERIALS = Set.of(Material.BEDROCK, Material.BARRIER, Material.END_PORTAL_FRAME, Material.END_PORTAL, Material.END_GATEWAY, Material.NETHER_PORTAL, Material.OBSIDIAN, Material.CRYING_OBSIDIAN);

    @ConfigEntry("piston-push-limit")
    @Comment("The maximum number of blocks a piston can move at once.")
    public static int PUSH_LIMIT = 12;
}
