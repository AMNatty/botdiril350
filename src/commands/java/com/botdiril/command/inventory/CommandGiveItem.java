package com.botdiril.command.inventory;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command(value = "give", aliases = { "giveitem", "givecard",
        "gift" }, category = CommandCategory.ITEMS, description = "Give someone an item or a card.", levelLock = 5)
public class CommandGiveItem
{
    private static final int MAX_LEVEL_DIFF = 100;

    @CmdInvoke
    public static void give(CommandContext co, @CmdPar(value = "player", type = ParType.ENTITY_NOT_SELF) EntityPlayer recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't give zero items.");

        if (item instanceof Item)
            ItemAssert.consumeItems(co.inventory, "gift items, this does not apply to cards", ItemPair.of(Items.timewarpCrystal));
        else if (item instanceof Card card && amount == co.inventory.howManyOf(card))
            throw new CommandException("You cannot give away all cards of the same type, try sending one less with `keepone` instead of `all` or `everyone`.");

        var recipientUI = recipient.inventory();

        CommandAssert.numberNotAboveL(Math.abs(recipientUI.getLevel() - co.inventory.getLevel()), MAX_LEVEL_DIFF, "*Sorry the level difference between you and the recipient is way too high! You can be **at most %d levels** apart.*".formatted(MAX_LEVEL_DIFF));

        co.userProperties.incrementStat(EnumStat.GIFTS_SENT);

        var eb = new ResponseEmbed();
        eb.setAuthor(co.player.getTag(), null, co.player.getAvatarURL());
        eb.setTitle("Gift");

        if (Curser.isCursed(co, EnumCurse.MAGNETIC) && BotdirilRnd.rollChance(0.5))
        {
            eb.setTitle("*%s Yoink!*".formatted(Icons.SCROLL_UNIQUE));
            recipient = co.botPlayer;
        }

        if (item instanceof Item iitem)
        {
            co.inventory.addItem(iitem, -amount);
            recipientUI.addItem(iitem, amount);
        }
        else if (item instanceof Card card)
        {
            co.inventory.addCard(card, -amount);
            recipientUI.addCard(card, amount);
        }

        eb.setDescription(String.format("You gave **%s** %s.", recipient.getMention(), BotdirilFmt.amountOfMD(amount, item.inlineDescription())));
        eb.setThumbnail(recipient.getAvatarURL());
        eb.setColor(0x008080);

        co.respond(eb);
    }

    @CmdInvoke
    public static void giveOne(CommandContext co, @CmdPar("player") EntityPlayer recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        if (item instanceof Item iitem)
        {
            CommandAssert.numberMoreThanZeroL(co.inventory.howManyOf(iitem), "You don't have that item to give it to someone.");
        }
        else if (item instanceof Card card)
        {
            CommandAssert.numberMoreThanZeroL(co.inventory.howManyOf(card), "You don't have that card to give it to someone.");
        }

        give(co, recipient, item, 1);
    }
}
