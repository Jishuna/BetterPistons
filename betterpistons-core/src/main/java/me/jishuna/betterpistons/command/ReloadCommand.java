package me.jishuna.betterpistons.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import me.jishuna.betterpistons.BetterPistons;
import me.jishuna.jishlib.MessageHandler;
import me.jishuna.jishlib.commands.SimpleCommandHandler;

public class ReloadCommand extends SimpleCommandHandler {
    private final BetterPistons plugin;

    protected ReloadCommand(BetterPistons plugin) {
        super("betterpistons.command.reload");
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String alias, String[] args) {
        plugin.reload();

        sender.sendMessage(MessageHandler.getInstance().getString("commands.reload.success"));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        return Collections.emptyList();
    }

}
