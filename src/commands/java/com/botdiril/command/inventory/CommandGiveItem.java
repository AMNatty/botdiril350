package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.util.BotdirilRnd;

@Command(value = "give", aliases = { "giveitem", "givecard",
        "gift" }, category = CommandCategory.ITEMS, description = "Give someone an item or a card.", levelLock = 5)
public class CommandGiveItem
{
    private static final int MAX_LEVEL_DIFF = 100;

    @CmdInvoke
    public static void give(CommandContext co, @CmdPar("user") User recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(amount, "You can't give zero items.");

        if (item instanceof Item)
            ItemAssert.consumeItems(co.ui, "gift items, this does not apply to cards", ItemPair.of(Items.timewarpCrystal));
        else if (item instanceof Card && amount == co.ui.howManyOf((Card) item))
            throw new CommandException("You cannot give away all cards of the same type, try sending one less with `keepone` instead of `all` or `everyone`.");

        if (recipient.getIdLong() == co.caller.getIdLong())
        {
            co.respond(String.format("You gave yourself **%d** %s(s)? ~~If there was a point in giving yourself your own stuff or something...~~", amount, item.inlineDescription()));
            return;
        }

        var recipientUI = new UserInventory(co.db, recipient.getIdLong());

        CommandAssert.numberNotAboveL(Math.abs(recipientUI.getLevel() - co.ui.getLevel()), MAX_LEVEL_DIFF, "*Sorry the level difference between you and the recipient is way too high! You can be **at most %d levels** apart.*".formatted(MAX_LEVEL_DIFF));

        co.po.incrementStat(EnumStat.GIFTS_SENT);

        var eb = new EmbedBuilder();
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());
        eb.setTitle("Gift");

        if (Curser.isCursed(co, EnumCurse.MAGNETIC) && BotdirilRnd.rollChance(0.5))
        {
            eb.setTitle("Yoink!");
            recipient = co.bot;
        }

        if (item instanceof Item)
        {
            co.ui.addItem((Item) item, -amount);
            recipientUI.addItem((Item) item, amount);
        }
        else if (item instanceof Card)
        {
            co.ui.addCard((Card) item, -amount);
            recipientUI.addCard((Card) item, amount);
        }

        eb.setDescription(String.format("You gave **%s** **%d** **%s**.", recipient.getAsMention(), amount, item.inlineDescription()));
        eb.setThumbnail(recipient.getEffectiveAvatarUrl());
        eb.setColor(0x008080);

        co.respond(eb);
    }

    @CmdInvoke
    public static void giveOne(CommandContext co, @CmdPar("user") User recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        if (item instanceof Item)
        {
            CommandAssert.numberMoreThanZeroL(co.ui.howManyOf((Item) item), "You don't have that item to give it to someone.");
        }
        else if (item instanceof Card)
        {
            CommandAssert.numberMoreThanZeroL(co.ui.howManyOf((Card) item), "You don't have that card to give it to someone.");
        }

        give(co, recipient, item, 1);
    }
}
