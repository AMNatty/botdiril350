package com.botdiril.command.general;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import net.dv8tion.jda.api.entities.Emote;

@Command("react")
public class CommandReact
{
    @CmdInvoke
    public static void react(DiscordCommandContext co, @CmdPar("emote") Emote emote)
    {
        co.message.addReaction(emote).submit();
    }
}
