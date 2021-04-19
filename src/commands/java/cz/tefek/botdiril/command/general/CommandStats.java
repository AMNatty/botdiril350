package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "stats", description = "Show your stats.", category = CommandCategory.GENERAL)
public class CommandStats
{
    @CmdInvoke
    public static void show(CommandContext co)
    {
        showStats(co, co.po, co.caller);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("user") User user)
    {
        var ui = new UserInventory(co.db, user.getIdLong());

        var po = new PropertyObject(co.db, ui.getFID());
        showStats(co, po, user);
    }

    private static void showStats(CommandContext co, PropertyObject po, User user)
    {
        var eb = new EmbedBuilder();

        eb.setTitle("Stats");
        eb.setColor(0x008080);
        eb.setDescription(user.getAsMention() + "'s stats.");

        Arrays.stream(EnumStat.values()).forEach(es -> eb.addField(es.getLocalizedName(), BotdirilFmt.format(po.getStat(es)), true));

        co.respond(eb);
    }
}
