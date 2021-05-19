package com.botdiril.command.inventory;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.EnumCurrency;
import com.botdiril.userdata.icon.IconUtil;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

import java.util.stream.Collectors;

@Command("iteminfo")
public class CommandItemInfo
{
    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("item") Item item)
    {
        var eb = new ResponseEmbed();
        eb.setTitle(item.inlineDescription());
        eb.setDescription(item.getDescription());
        eb.setColor(0x008080);

        if (co instanceof DiscordCommandContext dcc)
            eb.setThumbnail(IconUtil.urlFromIcon(dcc.jda, item));

        eb.addField("ID:", item.getName(), true);

        if (ShopEntries.canBeBought(item))
        {
            eb.addField("Buys for:", BotdirilFmt.amountOf(ShopEntries.getCoinPrice(item), EnumCurrency.COINS.getIcon()), true);
        }

        if (ShopEntries.canBeSold(item))
        {
            eb.addField("Sells for:",  BotdirilFmt.amountOf(ShopEntries.getSellValue(item), EnumCurrency.COINS.getIcon()), true);
        }

        if (ShopEntries.canBeBoughtForTokens(item))
        {
            eb.addField("Exchanges for:",  BotdirilFmt.amountOf(ShopEntries.getTokenPrice(item), EnumCurrency.TOKENS.getIcon()), true);
        }

        var recipe = CraftingEntries.search(item);

        if (recipe != null)
        {
            var components = recipe.components();
            var recipeParts = components.stream().map(ItemPair::toString).collect(Collectors.joining(" + "));
            eb.addField("Crafts from", """
            **%s**
            *Recipe yields %s item(s).*
            """.formatted(recipeParts, BotdirilFmt.format(recipe.amount())), false);
        }

        if (ShopEntries.canBeDisenchanted(item))
        {
            eb.addField("Disenchants for:",  BotdirilFmt.amountOf(ShopEntries.getDustForDisenchanting(item), EnumCurrency.DUST.getIcon()), false);
        }

        eb.setFooter(item.getFootnote(co), null);

        co.respond(eb);
    }
}
