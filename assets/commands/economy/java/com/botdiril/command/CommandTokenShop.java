package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.IGameObject;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

import java.text.MessageFormat;
import java.util.Comparator;

@Command("kekshop")
public class CommandTokenShop
{
    private static void addItems(ResponseEmbed eb, IGameObject item)
    {
        if (!ShopEntries.canBeBoughtForTokens(item))
        {
            return;
        }

        var sub = """
        **ID:** %s
        **Price:**: %s
        """.formatted(item.getName(), BotdirilFmt.amountOfMD(ShopEntries.getTokenPrice(item), Icons.TOKEN));

        eb.addField(item.getInlineDescription(), sub, true);
    }

    @CmdInvoke
    public static void shop(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setTitle("Black Market");
        eb.setDescription("""
        Use your %s to buy some items.
        You have %s.
        """.formatted(Icons.TOKEN, BotdirilFmt.amountOfMD(co.inventory.getKekTokens(), Icons.TOKEN)));
        eb.setColor(0x008080);

        Item.items().stream()
            .filter(ShopEntries::canBeBoughtForTokens)
            .sorted(Comparator.comparing(ShopEntries::getTokenPrice))
            .forEach(item -> addItems(eb, item));

        Card.cards().stream()
            .filter(ShopEntries::canBeBoughtForTokens)
            .sorted(Comparator.comparing(ShopEntries::getTokenPrice))
            .forEach(card -> addItems(eb, card));

        if (co instanceof ChatCommandContext cch)
            eb.setFooterText(MessageFormat.format("Tip: Use `{0}{1} <item> [amount]` or `{0}iteminfo <item>`.", cch.usedPrefix, cch.usedAlias));

        co.respond(eb);
    }
}
