package com.botdiril.command.currency;

import com.botdiril.Botdiril;
import com.botdiril.discord.framework.DiscordEntityPlayer;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.EnumCurrency;
import com.botdiril.userdata.achievement.Achievements;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilFmt;

@Command(value = "balance", aliases = { "money", "coins",
        "bal" }, category = CommandCategory.CURRENCY, description = "Shows your/someone's balance.")
public class CommandBalance
{
    @CmdInvoke
    public static void show(CommandContext co)
    {
        show(co, co.player);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        var ui = player.inventory();

        var uo = ui.getUserDataObj();

        var eb = new ResponseEmbed();
        eb.setTitle(player.getName() + "'s balance");
        eb.setThumbnail(player.getAvatarURL());

        var level = uo.level();
        var xp = uo.xp();

        eb.setDescription("Level %d".formatted(level));

        if (level != XPRewards.getMaxLevel())
        {
            eb.appendDescription("\n%s/%s XP (%.2f%%)".formatted(
                BotdirilFmt.format(xp),
                BotdirilFmt.amountOf(XPRewards.getXPAtLevel(level), Icons.XP),
                (double) xp / XPRewards.getXPAtLevel(level) * 100));
        }

        eb.appendDescription("\n\n");

        if (player instanceof DiscordEntityPlayer dep && co instanceof DiscordCommandContext dcc)
        {
            if (dep.getUserID() == Botdiril.AUTHOR_ID)
            {
                eb.appendDescription(Icons.CARD_MYTHIC).appendDescription(" **Lead Developer**\n");
            }

            if (ui.hasAchievement(Achievements.beta))
            {
                eb.appendDescription(Icons.ACHIEVEMENT_BETA).appendDescription(" **Beta Tester**\n");
            }

            var member = dep.getMember();

            if (member != null)
            {
                if (EnumPowerLevel.VIP_PLUS.check(co.db, member, dcc.textChannel))
                {
                    eb.appendDescription(Icons.CARD_UNIQUE).appendDescription(" **VIP+**\n");
                }

                if (EnumPowerLevel.VIP.check(co.db, member, dcc.textChannel))
                {
                    eb.appendDescription(Icons.CARD_LEGENDARY).appendDescription(" **VIP**\n");
                }
            }
        }

        eb.setColor(0x008080);

        eb.addField("%ss".formatted(EnumCurrency.COINS.getLocalizedName()), BotdirilFmt.amountOf(uo.coins(), EnumCurrency.COINS.getIcon()), true);
        eb.addField("%ss".formatted(EnumCurrency.KEKS.getLocalizedName()), BotdirilFmt.amountOf(uo.keks(), EnumCurrency.KEKS.getIcon()), true);
        eb.addField("%ss".formatted(EnumCurrency.MEGAKEKS.getLocalizedName()), BotdirilFmt.amountOf(uo.megakeks(), EnumCurrency.MEGAKEKS.getIcon()), true);
        eb.addField("%ss".formatted(EnumCurrency.TOKENS.getLocalizedName()), BotdirilFmt.amountOf(uo.tokens(), EnumCurrency.TOKENS.getIcon()), true);
        eb.addField("%ss".formatted(EnumCurrency.KEYS.getLocalizedName()), BotdirilFmt.amountOf(uo.keys(), EnumCurrency.KEYS.getIcon()), true);
        eb.addField("%ss".formatted(EnumCurrency.DUST.getLocalizedName()), BotdirilFmt.amountOf(uo.dust(), EnumCurrency.DUST.getIcon()), true);
        eb.addField("Cards", BotdirilFmt.amountOf(uo.cards(), Icons.CARDS), true);

        co.respond(eb);
    }
}
