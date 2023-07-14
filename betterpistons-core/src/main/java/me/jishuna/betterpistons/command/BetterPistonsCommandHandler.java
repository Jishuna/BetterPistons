package me.jishuna.betterpistons.command;

import me.jishuna.betterpistons.BetterPistons;
import me.jishuna.jishlib.MessageHandler;
import me.jishuna.jishlib.commands.ArgumentCommandHandler;

public class BetterPistonsCommandHandler extends ArgumentCommandHandler {

    public BetterPistonsCommandHandler(BetterPistons plugin) {
        super("betterpistons.command", () -> MessageHandler.getInstance().getString("commands.no-permission"), () -> MessageHandler.getInstance().getString("commands.usage"));

        addArgumentExecutor("reload", new ReloadCommand(plugin));
    }

}
