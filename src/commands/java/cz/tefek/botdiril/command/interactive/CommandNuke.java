package cz.tefek.botdiril.command.interactive;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.ItemAssert;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.util.BotdirilFmt;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(value = "nuke", category = CommandCategory.INTERACTIVE, description = "Literally nuke someone's keks.", levelLock = 30)
public class CommandNuke
{
    private static final long uraniumNeeded = 1000;
    private static final long toolboxesNeeded = 5;
    private static final long strangeMetalNeeded = 3;

    @CmdInvoke
    public static void nuke(CommandContext co, @CmdPar("user") Member member)
    {
        var userInv = new UserInventory(co.db, member.getUser().getIdLong());

        TimerUtil.require(co.ui, EnumTimer.STEAL, "You need to wait **$** before trying to **nuke** someone again (this command shares its cooldown with *steal*).");

        if (userInv.getKeks() < 100_000)
            throw new CommandException("This user is not worth nuking, try someone else with more %s.".formatted(Icons.KEK));

        ItemAssert.consumeItems(co.ui, "nuke someone",
            ItemPair.of(Items.uranium, uraniumNeeded), ItemPair.of(Items.toolBox, toolboxesNeeded), ItemPair.of(Items.strangeMetal, strangeMetalNeeded));

        var otherProps = new PropertyObject(co.db, userInv.getFID());

        if (Curser.isBlessed(otherProps, EnumBlessing.NUKE_IMMUNE))
        {
            var eb = new EmbedBuilder();
            eb.setTitle("Nuke");
            eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
            eb.setColor(0x008080);
            eb.setDescription(Icons.SCROLL_RARE + " That person is immune for some reason.\nTry someone else or wait.");

            throw new CommandException(eb);
        }

        if (BotdirilRnd.rollChance(0.5))
        {
            co.respond("**You missed.** " + Icons.KEK);
            return;
        }

        var keks = Math.min(userInv.getKeks(), Math.pow(co.ui.getLevel(), 1.8) * 5.0);
        var lost = Math.round(BotdirilRnd.RDG.nextUniform(0.01, 0.15) * keks);
        var stolen = Math.round(BotdirilRnd.RDG.nextUniform(0.05, 0.25) * keks);
        var nukeTotal = lost + stolen;

        userInv.addKeks(-nukeTotal);
        co.ui.addKeks(stolen);

        otherProps.incrementStat(EnumStat.TIMES_NUKED);

        if (co.po.getStat(EnumStat.BIGGEST_NUKE) < nukeTotal)
        {
            co.po.setStat(EnumStat.BIGGEST_NUKE, nukeTotal);
        }

        var eb = new EmbedBuilder();
        eb.setTitle("BOOM!");
        eb.setColor(0x008080);
        eb.setDescription("You nuked " + member.getAsMention() + "'s " + Icons.KEK + ".");
        eb.addField("Destroyed", String.format("**%s** %s", BotdirilFmt.format(lost), Icons.KEK), false);
        eb.addField("Stolen", String.format("**%s** %s", BotdirilFmt.format(stolen), Icons.KEK), false);

        co.respond(eb);
    }
}
