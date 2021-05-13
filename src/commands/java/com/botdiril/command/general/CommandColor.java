package com.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.util.BotdirilRnd;

@Command(value = "color", category = CommandCategory.GENERAL, aliases = { "col" }, description = "Gets a random color.")
public class CommandColor
{
    @CmdInvoke
    public static void choose(CommandContext co)
    {
        var col = BotdirilRnd.RDG.nextInt(0x000000, 0xffffff);

        var eb = new EmbedBuilder();
        eb.setTitle("Here's your random color.");
        eb.setColor(col);
        var hexInt = Integer.toHexString(col);
        var hex = "000000".substring(hexInt.length()) + hexInt;
        eb.setDescription("#" + hex);
        co.respond(eb);
    }
}
