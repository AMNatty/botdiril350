package com.botdiril.command;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;
import net.dv8tion.jda.api.EmbedBuilder;

@Command("nuke")
public class CommandNuke
{
    private static final long uraniumNeeded = 1000;
    private static final long toolboxesNeeded = 5;
    private static final long strangeMetalNeeded = 3;

    private static final double MISS_CHANCE = 0.5;

    @CmdInvoke
    public static void nuke(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        var userInv = player.inventory();

        TimerUtil.require(co.inventory, EnumTimer.STEAL, "You need to wait **$** before trying to **nuke** someone again (this command shares its cooldown with *steal*).");

        if (userInv.getKeks() < 100_000)
            throw new CommandException("This user is not worth nuking, try someone else with more %s.".formatted(Icons.KEK));

        ItemAssert.consumeItems(co.inventory, "nuke someone",
            ItemPair.of(Items.uranium, uraniumNeeded), ItemPair.of(Items.toolBox, toolboxesNeeded), ItemPair.of(Items.strangeMetal, strangeMetalNeeded));

        var otherProps = userInv.getPropertyObject();

        if (Curser.isBlessed(otherProps, EnumBlessing.NUKE_IMMUNE))
        {
            var eb = new EmbedBuilder();
            eb.setTitle("Nuke");
            eb.setThumbnail(co.botIconURL);
            eb.setColor(0x008080);
            eb.setDescription(Icons.SCROLL_RARE + " That person is immune for some reason.\nTry someone else or wait.");

            throw new CommandException(eb);
        }

        if (player.equals(co.player))
        {
            co.respond("**You nuked yourself, good job.**");

            for (int i = 0; i < 10; i++)
                Curser.curse(co);
        }
        else if (BotdirilRnd.rollChance(co.rdg, MISS_CHANCE))
        {
            co.respond("**You missed.** " + Icons.KEK);
            return;
        }
        else
        {
            Curser.curse(co, player);
        }

        var keks = Math.min(userInv.getKeks(), Math.pow(co.inventory.getLevel() * 50.0, 1.8));
        var lost = Math.round(co.rdg.nextUniform(0.01, 0.15) * keks);
        var stolen = Math.round(co.rdg.nextUniform(0.05, 0.25) * keks);
        var nukeTotal = lost + stolen;

        userInv.addKeks(-nukeTotal);
        co.inventory.addKeks(stolen);

        otherProps.incrementStat(EnumStat.TIMES_NUKED);

        if (co.userProperties.getStat(EnumStat.BIGGEST_NUKE) < nukeTotal)
        {
            co.userProperties.setStat(EnumStat.BIGGEST_NUKE, nukeTotal);
        }

        var eb = new ResponseEmbed();
        eb.setTitle("BOOM!");
        eb.setColor(0x008080);
        eb.setDescription("You nuked %s's %s.".formatted(player.getMention(), Icons.KEK));
        eb.addField("Destroyed", BotdirilFmt.amountOfMD(lost, Icons.KEK), false);
        eb.addField("Stolen", BotdirilFmt.amountOfMD(stolen, Icons.KEK), false);

        co.respond(eb);
    }
}
