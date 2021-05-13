package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.IconUtil;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.ShopEntries;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed.Field;

import java.util.stream.Collectors;

import com.botdiril.userdata.EnumCurrency;
import com.botdiril.userdata.card.EnumCardModifier;
import com.botdiril.util.BotdirilFmt;

@Command(value = "cardinfo", aliases = {
        "ci" }, category = CommandCategory.ITEMS, description = "Shows important information about a card")
public class CommandCardInfo
{
    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("card") Card card)
    {
        var eb = new EmbedBuilder();
        eb.setDescription(card.getDescription());
        eb.setColor(0x008080);

        eb.setThumbnail(IconUtil.urlFromIcon(co.jda, card));

        eb.setDescription(card.getDescription());

        eb.addField("ID:", card.getName(), true);

        eb.addField("Set:", card.getCardSet().getSetLocalizedName(), true);

        eb.addField("Rarity:", card.getCardRarity().getCardIcon() + " " + card.getCardRarity().getRarityName(), true);

        boolean hasCard = co.ui.howManyOf(card) > 0;

        if (hasCard)
        {
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

            eb.addField(new Field("Sells for:", "%s %s".formatted(BotdirilFmt.format(Card.getPrice(card, level)), EnumCurrency.COINS.getIcon()), true));
        }
        else
        {
            eb.setTitle("%s".formatted(card.getLocalizedName()));

            eb.addField("XP / Level", "Obtain this card to level it up!" , true);

            eb.addField(new Field("Sells for:", "%s %s".formatted(BotdirilFmt.format(Card.getPrice(card, 0)), EnumCurrency.COINS.getIcon()), true));
        }

        if (card.hasCollection())
        {
            eb.addField("Collection:", card.getCollectionName(), true);
        }

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

        co.respond(eb);
    }
}
