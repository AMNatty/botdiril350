package com.botdiril.command.debug;

import net.dv8tion.jda.api.EmbedBuilder;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;

@Command(value = "memory",
    aliases = { "heap", "ram" },
    category = CommandCategory.SUPERUSER,
    description = "Shows some memory information.")
public class CommandRAM
{
    @CmdInvoke
    public static void print(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Botdiril Debug Commands", null, co.bot.getEffectiveAvatarUrl());
        eb.setTitle("Memory information.");
        eb.setColor(0x008080);

        var rt = Runtime.getRuntime();

        eb.addField("Maximum memory", rt.maxMemory() / 1000000 + " MB", false);
        eb.addField("Used memory", rt.totalMemory() / 1000000 + " MB", false);
        eb.addField("Free memory", rt.freeMemory() / 1000000 + " MB", false);

        co.respond(eb);
    }
}
