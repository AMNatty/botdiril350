package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.achievement.Achievements;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.xp.XPRewards;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "balance", aliases = { "money", "coins",
        "bal" }, category = CommandCategory.CURRENCY, description = "Shows your/someone's balance.")
public class CommandBalance
{
    @CmdInvoke
    public static void show(CallObj co)
    {
        show(co, co.callerMember);
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("user") Member member)
    {
        var u = member.getUser();
        var userID = u.getIdLong();
        var sameGuy = userID == co.caller.getIdLong();

        var ui = sameGuy ? co.ui : new UserInventory(co.db, userID);

        var uo = ui.getUserDataObj();

        var eb = new EmbedBuilder();
        eb.setTitle(u.getName() + "'s balance");
        eb.setThumbnail(u.getEffectiveAvatarUrl());

        var level = uo.getLevel();
        var xp = uo.getXP();

        String desc = String.format("Level %d", level);

        if (level != XPRewards.getMaxLevel())
        {
            desc += String.format("\n%s/%s xp (%.2f%%)",
                BotdirilFmt.format(xp),
                BotdirilFmt.format(XPRewards.getXPAtLevel(level)),
                (double) xp / XPRewards.getXPAtLevel(level) * 100);
        }

        desc += "\n\n";

        if (userID == Botdiril.AUTHOR_ID)
        {
            desc += Icons.CARD_MYTHIC + " **Lead Developer**\n";
        }

        if (ui.hasAchievement(Achievements.beta))
        {
            desc += Icons.ACHIEVEMENT_BETA + " **Beta Tester**\n";
        }

        if (EnumPowerLevel.VIP_PLUS.check(co.db, member, co.textChannel))
        {
            desc += Icons.CARD_UNIQUE + " **VIP+**\n";
        }

        if (EnumPowerLevel.VIP.check(co.db, member, co.textChannel))
        {
            desc += Icons.CARD_LEGENDARY + " **VIP**\n";
        }

        eb.setDescription(desc);

        eb.setColor(0x008080);

        eb.addField(new Field(EnumCurrency.COINS.getLocalizedName(), String.format("%s %s\n", BotdirilFmt.format(uo.getCoins()), EnumCurrency.COINS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.KEKS.getLocalizedName(), String.format("%s %s\n", BotdirilFmt.format(uo.getKeks()), EnumCurrency.KEKS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.MEGAKEKS.getLocalizedName(), String.format("%s %s\n", BotdirilFmt.format(uo.getMegaKeks()), EnumCurrency.MEGAKEKS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.TOKENS.getLocalizedName(), String.format("%s %s\n", BotdirilFmt.format(uo.getTokens()), EnumCurrency.TOKENS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.KEYS.getLocalizedName(), String.format("%s %s\n", BotdirilFmt.format(uo.getKeys()), EnumCurrency.KEYS.getIcon()), true));
        eb.addField(new Field(EnumCurrency.DUST.getLocalizedName(), String.format("%s %s\n", BotdirilFmt.format(uo.getDust()), EnumCurrency.DUST.getIcon()), true));
        eb.addField(new Field("Cards", String.format("%d %s\n", uo.getCards(), Icons.CARDS), true));

        co.respond(eb.build());
    }
}