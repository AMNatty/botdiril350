package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.Item;
import com.botdiril.util.BotdirilFmt;

@Command(value = "howmanyof", aliases = { "countcards", "countitems", "cardcount", "itemcount", "cc", "ic",
        "amount" }, category = CommandCategory.ITEMS, description = "Tells the count of an item or card.")
public class CommandHowManyOf
{
    @CmdInvoke
    public static void count(CommandContext co, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable thing)
    {
        long count;

        if (thing instanceof Card)
        {
            count = co.ui.howManyOf((Card) thing);
        }
        else if (thing instanceof Item)
        {
            count = co.ui.howManyOf((Item) thing);
        }
        else
        {
            co.respond("**An error has happened while processing this command:** Invalid instance of IIdentifiable.\n**Please contact the developer.**");
            return;
        }

        if (count <= 0)
        {
            co.respond(String.format("You have **no %s**.", thing.inlineDescription()));
        }
        else
        {
            co.respond(String.format("You have **%s %s**.", BotdirilFmt.format(count), thing.inlineDescription()));
        }
    }
}
