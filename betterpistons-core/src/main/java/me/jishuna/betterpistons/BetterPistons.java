package me.jishuna.betterpistons;

import java.io.File;

import org.bukkit.plugin.java.JavaPlugin;

import me.jishuna.betterpistons.command.BetterPistonsCommandHandler;
import me.jishuna.betterpistons.nms.NMSManager;
import me.jishuna.jishlib.MessageHandler;
import me.jishuna.jishlib.config.ConfigReloadable;
import me.jishuna.jishlib.config.ConfigurationManager;

public class BetterPistons extends JavaPlugin {
    private ConfigReloadable<Settings> settingsReloadable;

    @Override
    public void onLoad() {
        NMSManager.initAdapater(this);
        NMSManager.getAdapter().onLoad(this);
    }

    @Override
    public void onEnable() {
        ConfigurationManager manager = new ConfigurationManager(this);
        MessageHandler.initalize(manager, new File(getDataFolder(), "messages.yml"), getResource("messages.yml"));

        settingsReloadable = manager.createStaticReloadable(new File(getDataFolder(), "config.yml"), Settings.class);
        settingsReloadable.saveDefaults().load();

        getCommand("betterpistons").setExecutor(new BetterPistonsCommandHandler(this));
    }

    public void reload() {
        this.settingsReloadable.load();
    }
}
