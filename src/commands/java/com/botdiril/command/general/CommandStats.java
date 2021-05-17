package com.botdiril.command.general;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.util.BotdirilFmt;

import java.util.Arrays;

@Command("stats")
public class CommandStats
{
    @CmdInvoke
    public static void show(CommandContext co)
    {
        showStats(co, co.player);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        showStats(co, player);
    }

    private static void showStats(CommandContext co, EntityPlayer player)
    {
        var eb = new ResponseEmbed();

        eb.setTitle("Stats");
        eb.setColor(0x008080);
        eb.setDescription(player.getMention() + "'s stats.");

        var po = player.inventory().getPropertyObject();

        Arrays.stream(EnumStat.values()).forEach(es -> eb.addField(es.getLocalizedName(), BotdirilFmt.format(po.getStat(es)), true));

        co.respond(eb);
    }
}
