package com.botdiril.command;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandManager;
import com.botdiril.framework.command.invoke.CmdInvoke;

@Command("unload")
public class CommandUnload
{
    @CmdInvoke
    public static void unload(DiscordCommandContext dcc)
    {
        var textChannel = dcc.textChannel;
        CommandManager.unload(() -> textChannel.sendMessage("**Unload complete.**").queue());
    }
}