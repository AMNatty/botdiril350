package com.botdiril.command.debug;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;

@Command("memory")
public class CommandRAM
{
    @CmdInvoke
    public static void print(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setAuthor("Botdiril Debug Commands", null, co.botIconURL);
        eb.setTitle("Memory information.");
        eb.setColor(0x008080);

        var rt = Runtime.getRuntime();

        eb.addField("Maximum memory", rt.maxMemory() / 1000000 + " MB", false);
        eb.addField("Used memory", rt.totalMemory() / 1000000 + " MB", false);
        eb.addField("Free memory", rt.freeMemory() / 1000000 + " MB", false);

        co.respond(eb);
    }
}
