package com.botdiril.command.debug;

import net.dv8tion.jda.api.EmbedBuilder;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;

@Command(aliases = {
        "latency", "heartbeat" }, category = CommandCategory.GENERAL, description = "Tells the Discord latency.", value = "ping")
public class CommandPing
{
    @CmdInvoke
    public static void ping(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Botdiril Debug Commands", null, co.bot.getEffectiveAvatarUrl());
        eb.setTitle("Pong.");
        eb.setColor(0x008080);
        eb.setDescription(co.jda.getGatewayPing() + " ms");

        co.respond(eb);
    }
}
