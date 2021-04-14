package cz.tefek.botdiril.command.interactive;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.preferences.EnumUserPreference;
import cz.tefek.botdiril.userdata.preferences.UserPreferences;
import cz.tefek.botdiril.userdata.properties.PropertyObject;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilFmt;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(aliases = {
        "rob" }, category = CommandCategory.INTERACTIVE, description = "Hehe. Time to rob someone.", value = "steal", levelLock = 10)
public class CommandSteal
{
    @CmdInvoke
    public static void steal(CallObj co, @CmdPar("who to rob") User user)
    {
        TimerUtil.require(co.ui, EnumTimer.STEAL, "You need to wait **$** before trying to **steal** again.");

        CommandAssert.assertNotEquals(co.caller.getIdLong(), user.getIdLong(), "You can't rob yourself. Or can you? :thinking:");

        CommandAssert.numberNotBelowL(co.ui.getCoins(), 1000, "You need at least **1'000** " + Icons.COIN + " to rob someone.");

        var userLevel = co.ui.getLevel();
        var lvlData = XPRewards.getLevel(userLevel);

        var other = new UserInventory(co.db, user.getIdLong());

        var lvlOther = other.getLevel();

        var lvlMod = Math.pow(2.5, -Math.abs(lvlOther - userLevel) / 100.0);

        var maxSteal = Math.round(Math.pow(userLevel * 100, 1.8) * lvlMod);

        var mod = BotdirilRnd.RANDOM.nextDouble() * 0.8 - 0.4;

        var otherProps = new PropertyObject(co.db, other.getFID());

        if (Curser.isCursed(otherProps, EnumCurse.EASIER_TO_ROB))
        {
            mod = BotdirilRnd.RANDOM.nextDouble() * 0.6 - 0.2;
        }

        if (Curser.isBlessed(otherProps, EnumBlessing.STEAL_IMMUNE))
        {
            co.ui.resetTimer(EnumTimer.STEAL);

            var eb = new EmbedBuilder();
            eb.setTitle("Steal");
            eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
            eb.setColor(0x008080);
            eb.setDescription("That person is immune. For some reason.");
            co.respond(eb.build());

            return;
        }

        var eb = new EmbedBuilder();
        eb.setTitle("Steal");
        eb.setThumbnail(co.bot.getEffectiveAvatarUrl());
        eb.setColor(0x008080);

        if (co.ui.howManyOf(Items.toolBox) > 0 && !UserPreferences.isBitEnabled(co.po, EnumUserPreference.TOOLBOX_STEALING_DISABLED))
        {
            eb.appendDescription(MessageFormat.format("You used a **{0}**...\n", Items.toolBox.inlineDescription()));
            co.ui.addItem(Items.toolBox, -1);
            mod = Math.abs(mod);
        }

        if (Curser.isBlessed(co, EnumBlessing.PICKPOCKET) && BotdirilRnd.rollChance(0.5))
        {
            maxSteal *= 100;
        }

        var stole = Math.min(Math.round(other.getCoins() * mod), maxSteal);

        if (stole <= 0)
        {
            eb.appendDescription("You didn't manage to steal anything, better luck next time...\n;)");
        }
        else
        {
            if (co.po.getStat(EnumStat.BIGGEST_STEAL) < stole)
            {
                co.po.setStat(EnumStat.BIGGEST_STEAL, stole);
            }

            co.ui.addCoins(stole);
            other.addCoins(-stole);
            eb.appendDescription("It worked out!");
            eb.addField("Stolen coins", String.format("**%s %s**.", BotdirilFmt.format(stole), Icons.COIN), false);

            otherProps.incrementStat(EnumStat.TIMES_ROBBED);
        }

        var xp = Math.round(BotdirilRnd.RANDOM.nextDouble() * (lvlData.getDailyMax() - lvlData.getDailyMin()) + lvlData.getDailyMin()) / 15;
        eb.addField("Rewards", String.format("**%s XP**", BotdirilFmt.format(xp)), false);
        co.ui.addXP(co, xp);

        co.respond(eb.build());
    }
}
