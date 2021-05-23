package com.botdiril.command;

import net.dv8tion.jda.api.EmbedBuilder;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.invoke.CmdInvoke;

@Command("ping")
public class CommandPing
{
    @CmdInvoke
    public static void ping(DiscordCommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Botdiril Debug Commands", null, co.bot.getEffectiveAvatarUrl());
        eb.setTitle("Pong.");
        eb.setColor(0x008080);
        eb.setDescription(co.jda.getGatewayPing() + " ms");

        co.respond(eb);
    }
}
