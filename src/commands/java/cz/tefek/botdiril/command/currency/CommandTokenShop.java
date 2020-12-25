package cz.tefek.botdiril.command.currency;

import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Comparator;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "illegalkekmarket", aliases = { "kekmarket", "blackmarket",
        "kekshop" }, category = CommandCategory.CURRENCY, description = "Visit the illegal kek market.")
public class CommandTokenShop
{
    private static void addItems(EmbedBuilder eb, IIdentifiable item)
    {
        if (!ShopEntries.canBeBoughtForTokens(item))
        {
            return;
        }

        String sub = "**ID:** " +
                     item.getName() +
                     "\n**Price:** " +
                     BotdirilFmt.format(ShopEntries.getTokenPrice(item)) +
                     Icons.TOKEN +
                     "\n";
        eb.addField(item.inlineDescription(), sub, true);
    }

    @CmdInvoke
    public static void shop(CallObj co)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Black Market");
        eb.setDescription(String.format("Use your %s to buy some items.\nYou have **%s** %s.", Icons.TOKEN, BotdirilFmt.format(co.ui.getKekTokens()), Icons.TOKEN));
        eb.setColor(0x008080);

        Item.items().stream()
            .filter(ShopEntries::canBeBoughtForTokens)
            .sorted(Comparator.comparing(ShopEntries::getTokenPrice))
            .forEach(item -> addItems(eb, item));

        Card.cards().stream()
            .filter(ShopEntries::canBeBoughtForTokens)
            .sorted(Comparator.comparing(ShopEntries::getTokenPrice))
            .forEach(card -> addItems(eb, card));

        eb.setFooter("Tip: Use `%sexchange <item> [amount]` or `%siteminfo <item>`.".replace("%s", co.usedPrefix), null);

        co.respond(eb.build());
    }
}
