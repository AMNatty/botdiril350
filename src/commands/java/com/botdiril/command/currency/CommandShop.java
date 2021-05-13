package com.botdiril.command.currency;

import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Comparator;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

@Command(value = "shop", aliases = { "store",
        "market" }, category = CommandCategory.CURRENCY, description = "Opens the shops.")
public class CommandShop
{
    @CmdInvoke
    public static void shop(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Botdiril's Shop");
        eb.setDescription(String.format("You have **%s** %s.", BotdirilFmt.format(co.ui.getCoins()), Icons.COIN));
        eb.setColor(0x008080);

        Item.items().stream()
            .filter(ShopEntries::canBeBought)
            .sorted(Comparator.comparing(ShopEntries::getCoinPrice))
            .forEach(item -> addItems(eb, item));

        eb.setFooter("Tip: Use `%sbuy <item> [amount]`, `%ssell <item> [amount]` or `%siteminfo <item>`.".replace("%s", co.usedPrefix), null);

        co.respond(eb);
    }

    private static void addItems(EmbedBuilder eb, Item item)
    {
        StringBuilder sub = new StringBuilder();

        sub.append("**ID:** ");
        sub.append(item.getName());
        sub.append("\n**Price:** ");
        sub.append(BotdirilFmt.format(ShopEntries.getCoinPrice(item)));
        sub.append(Icons.COIN);
        sub.append("\n");
        if (ShopEntries.canBeSold(item))
        {
            sub.append("**Sells back for:** ");
            sub.append(BotdirilFmt.format(ShopEntries.getSellValue(item)));
            sub.append(Icons.COIN);
        }
        else
        {
            sub.append("*Cannot be sold.*");
        }

        eb.addField(item.inlineDescription(), sub.toString(), true);
    }
}
