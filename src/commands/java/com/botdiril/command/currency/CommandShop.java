package com.botdiril.command.currency;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

import java.text.MessageFormat;
import java.util.Comparator;

@Command("shop")
public class CommandShop
{
    @CmdInvoke
    public static void shop(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setTitle("%s's Shop".formatted(Botdiril.BRANDING));
        eb.setDescription(String.format("You have **%s** %s.", BotdirilFmt.format(co.inventory.getCoins()), Icons.COIN));
        eb.setColor(0x008080);

        Item.items().stream()
            .filter(ShopEntries::canBeBought)
            .sorted(Comparator.comparing(ShopEntries::getCoinPrice))
            .forEach(item -> addItems(eb, item));

        if (co instanceof ChatCommandContext cch)
            eb.setFooterText(MessageFormat.format("Tip: Use `{0}{1} <item> [amount]`, `{0}sell <item> [amount]` or `{0}iteminfo <item>`.", cch.usedPrefix, cch.usedAlias));

        co.respond(eb);
    }

    private static void addItems(ResponseEmbed eb, Item item)
    {
        StringBuilder sub = new StringBuilder();

        sub.append("""
        **ID:** %s
        **Price:** %s
        
        """.formatted(item.getName(), BotdirilFmt.amountOfMD(ShopEntries.getCoinPrice(item), Icons.COIN)));

        if (ShopEntries.canBeSold(item))
        {
            sub.append("**Sells back for:** ");
            sub.append(BotdirilFmt.amountOf(ShopEntries.getSellValue(item), Icons.COIN));
        }
        else
        {
            sub.append("*Cannot be sold.*");
        }

        eb.addField(item.inlineDescription(), sub.toString(), true);
    }
}
