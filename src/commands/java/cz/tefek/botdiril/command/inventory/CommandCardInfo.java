package cz.tefek.botdiril.command.inventory;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.userdata.EnumCurrency;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.card.EnumCardModifier;
import cz.tefek.botdiril.userdata.icon.IconUtil;
import cz.tefek.botdiril.userdata.item.CraftingEntries;
import cz.tefek.botdiril.userdata.item.ShopEntries;
import cz.tefek.botdiril.util.BotdirilFmt;

@Command(value = "cardinfo", aliases = {
        "ci" }, category = CommandCategory.ITEMS, description = "Shows important information about a card")
public class CommandCardInfo
{
    @CmdInvoke
    public static void show(CallObj co, @CmdPar("card") Card card)
    {
        var eb = new EmbedBuilder();
        eb.setDescription(card.getDescription());
        eb.setColor(0x008080);

        eb.setThumbnail(IconUtil.urlFromIcon(co.jda, card));

        eb.setDescription(card.getDescription());

        eb.addField("ID:", card.getName(), true);

        eb.addField("Set:", card.getCardSet().getSetLocalizedName(), true);

        eb.addField("Rarity:", card.getCardRarity().getCardIcon() + " " + card.getCardRarity().getRarityName(), true);

        var level = co.ui.getCardLevel(card);
        var tier = EnumCardModifier.getByLevel(level);
        assert tier != null;
        eb.setTitle("%s %s".formatted(tier.getLocalizedName(), card.getLocalizedName()));
        eb.addField("Level:", BotdirilFmt.format(level), true);

        var xpForLevelUp = tier.getXPForLevelUp();

        if (xpForLevelUp == Long.MAX_VALUE)
            eb.addField("XP:", BotdirilFmt.format(co.ui.getCardXP(card)), true);
        else
            eb.addField("XP:", "%s / %s".formatted(BotdirilFmt.format(co.ui.getCardXP(card)), BotdirilFmt.format(xpForLevelUp)), true);

        if (card.hasCollection())
        {
            eb.addField("Collection:", card.getCollectionName(), true);
        }

        eb.addField(new Field("Sells for:", "%s %s".formatted(BotdirilFmt.format(Card.getPrice(card, level)), EnumCurrency.COINS.getIcon()), true));

        var recipe = CraftingEntries.search(card);

        if (recipe != null)
        {
            var components = recipe.getComponents();
            var recipeParts = components.stream().map(comp ->
                String.format("**%s %s**", BotdirilFmt.format(comp.getAmount()), comp.getItem().inlineDescription())).collect(Collectors.joining(" + "));

            eb.addField("Crafts from", recipeParts + "\n*Recipe yields " + BotdirilFmt.format(recipe.getAmount()) + " item(s).*", false);
        }

        if (ShopEntries.canBeDisenchanted(card))
        {
            eb.addField(new Field("Disenchants for:", BotdirilFmt.format(ShopEntries.getDustForDisenchanting(card)) + " " + EnumCurrency.DUST.getIcon(), true));
        }

        eb.setFooter(card.getFootnote(co), null);

        if (card.hasCustomImage())
        {
            eb.setImage(card.getCustomImage());
        }

        co.respond(eb.build());
    }
}
