package com.botdiril.command;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandManager;
import com.botdiril.framework.command.invoke.CmdInvoke;

@Command("reload")
public class CommandReload
{
    @CmdInvoke
    public static void resetItem(DiscordCommandContext dcc)
    {
        var textChannel = dcc.textChannel;
        CommandManager.unload(() -> {
            CommandManager.load();
            textChannel.sendMessage("**Reload complete.**").queue();
        });
    }
}