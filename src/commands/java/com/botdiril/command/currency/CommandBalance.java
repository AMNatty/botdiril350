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

        String desc = String.format("Level %d", level);

        if (level != XPRewards.getMaxLevel())
        {
            desc += String.format("\n%s/%s XP (%.2f%%)",
                BotdirilFmt.format(xp),
                BotdirilFmt.amountOf(XPRewards.getXPAtLevel(level), Icons.XP),
                (double) xp / XPRewards.getXPAtLevel(level) * 100);
        }

        desc += "\n\n";

        if (player instanceof DiscordEntityPlayer dep && co instanceof DiscordCommandContext dcc)
        {
            if (dep.getUserID() == Botdiril.AUTHOR_ID)
            {
                desc += Icons.CARD_MYTHIC + " **Lead Developer**\n";
            }

            if (ui.hasAchievement(Achievements.beta))
            {
                desc += Icons.ACHIEVEMENT_BETA + " **Beta Tester**\n";
            }

            var member = dep.getMember();

            if (member != null)
            {
                if (EnumPowerLevel.VIP_PLUS.check(co.db, member, dcc.textChannel))
                {
                    desc += Icons.CARD_UNIQUE + " **VIP+**\n";
                }

                if (EnumPowerLevel.VIP.check(co.db, member, dcc.textChannel))
                {
                    desc += Icons.CARD_LEGENDARY + " **VIP**\n";
                }
            }
        }

        eb.setDescription(desc);

        eb.setColor(0x008080);

        eb.addField(EnumCurrency.COINS.getLocalizedName(), BotdirilFmt.amountOf(uo.coins(), EnumCurrency.COINS.getIcon()), true);
        eb.addField(EnumCurrency.KEKS.getLocalizedName(), BotdirilFmt.amountOf(uo.keks(), EnumCurrency.KEKS.getIcon()), true);
        eb.addField(EnumCurrency.MEGAKEKS.getLocalizedName(), BotdirilFmt.amountOf(uo.megakeks(), EnumCurrency.MEGAKEKS.getIcon()), true);
        eb.addField(EnumCurrency.TOKENS.getLocalizedName(), BotdirilFmt.amountOf(uo.tokens(), EnumCurrency.TOKENS.getIcon()), true);
        eb.addField(EnumCurrency.KEYS.getLocalizedName(), BotdirilFmt.amountOf(uo.keys(), EnumCurrency.KEYS.getIcon()), true);
        eb.addField(EnumCurrency.DUST.getLocalizedName(), BotdirilFmt.amountOf(uo.dust(), EnumCurrency.DUST.getIcon()), true);
        eb.addField("Cards", BotdirilFmt.amountOf(uo.cards(), Icons.CARDS), true);

        co.respond(eb);
    }
}
