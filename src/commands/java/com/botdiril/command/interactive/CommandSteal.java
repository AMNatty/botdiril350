package com.botdiril.command.interactive;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command(aliases = {
        "rob" }, category = CommandCategory.INTERACTIVE, description = "Hehe. Time to rob someone.", value = "steal", levelLock = 10)
public class CommandSteal
{
    private static final long MIN_TO_STEAL = 1000;

    @CmdInvoke
    public static void steal(CommandContext co, @CmdPar(value = "player to rob", type = ParType.ENTITY_NOT_SELF) EntityPlayer player)
    {
        TimerUtil.require(co.inventory, EnumTimer.STEAL, "You need to wait **$** before trying to **steal** again.");

        CommandAssert.numberNotBelowL(co.inventory.getCoins(), MIN_TO_STEAL, "You need at least %s to rob someone.".formatted(BotdirilFmt.amountOfMD(MIN_TO_STEAL, Icons.COIN)));

        var userLevel = co.inventory.getLevel();
        var lvlData = XPRewards.getLevel(userLevel);

        var other = player.inventory();

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
            co.inventory.resetTimer(EnumTimer.STEAL);

            var eb = new ResponseEmbed();
            eb.setTitle("Steal");
            eb.setThumbnail(co.botIconURL);
            eb.setColor(0x008080);
            eb.setDescription("That person is immune. For some reason.");
            co.respond(eb);

            return;
        }

        var eb = new ResponseEmbed();
        eb.setTitle("Steal");
        eb.setThumbnail(co.botIconURL);
        eb.setColor(0x008080);

        if (co.inventory.howManyOf(Items.toolBox) > 0 && !UserPreferences.isBitEnabled(co.userProperties, EnumUserPreference.TOOLBOX_STEALING_DISABLED))
        {
            eb.appendDescription("You used **%s**...\n".formatted(BotdirilFmt.amountOfMD("a", Icons.ITEM_MISC_TOOLBOX)));
            co.inventory.addItem(Items.toolBox, -1);
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
            if (co.userProperties.getStat(EnumStat.BIGGEST_STEAL) < stole)
            {
                co.userProperties.setStat(EnumStat.BIGGEST_STEAL, stole);
            }

            co.inventory.addCoins(stole);
            other.addCoins(-stole);
            eb.appendDescription("It worked out!");
            eb.addField("Stolen coins", BotdirilFmt.amountOfMD(stole, Icons.COIN), false);

            otherProps.incrementStat(EnumStat.TIMES_ROBBED);
        }

        var xp = Math.round(BotdirilRnd.RANDOM.nextDouble() * (lvlData.getDailyMax() - lvlData.getDailyMin()) + lvlData.getDailyMin()) / 15;
        eb.addField("Rewards", BotdirilFmt.amountOfMD(xp, Icons.XP), false);
        co.inventory.addXP(co, xp);

        co.respond(eb);
    }
}
