package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

import java.util.stream.Collectors;

import com.botdiril.userdata.EnumCurrency;
import com.botdiril.userdata.icon.IconUtil;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;

@Command(value = "iteminfo", aliases = {
        "ii" }, category = CommandCategory.ITEMS, description = "Shows important information about an item")
public class CommandItemInfo
{
    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("item") Item item)
    {
        var eb = new EmbedBuilder();
        eb.setTitle(item.inlineDescription());
        eb.setDescription(item.getDescription());
        eb.setColor(0x008080);

        eb.setThumbnail(IconUtil.urlFromIcon(co.jda, item));

        eb.addField("ID:", item.getName(), true);

        if (ShopEntries.canBeBought(item))
        {
            eb.addField(new Field("Buys for:", BotdirilFmt.format(ShopEntries.getCoinPrice(item)) + " " + EnumCurrency.COINS.getIcon(), true));
        }

        if (ShopEntries.canBeSold(item))
        {
            eb.addField(new Field("Sells for:", BotdirilFmt.format(ShopEntries.getSellValue(item)) + " " + EnumCurrency.COINS.getIcon(), true));
        }

        if (ShopEntries.canBeBoughtForTokens(item))
        {
            eb.addField(new Field("Exchanges for:", BotdirilFmt.format(ShopEntries.getTokenPrice(item)) + " " + EnumCurrency.TOKENS.getIcon(), true));
        }

        var recipe = CraftingEntries.search(item);

        if (recipe != null)
        {
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp -> String.format("**%s %s**", BotdirilFmt.format(comp.getAmount()), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));
            eb.addField("Crafts from", recipeParts + "\n*Recipe yields " + BotdirilFmt.format(recipe.getAmount()) + " item(s).*", false);
        }

        if (ShopEntries.canBeDisenchanted(item))
        {
            eb.addField(new Field("Disenchants for:", BotdirilFmt.format(ShopEntries.getDustForDisenchanting(item)) + " " + EnumCurrency.DUST.getIcon(), false));
        }

        eb.setFooter(item.getFootnote(co), null);

        co.respond(eb);
    }
}
