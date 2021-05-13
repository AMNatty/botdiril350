package com.botdiril.command.interactive;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.IOpenable;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.util.BotdirilRnd;

@Command(value = "open", aliases = {
        "use" }, category = CommandCategory.INTERACTIVE, description = "Open a card pack/crate/something else.")
public class CommandOpen
{
    @CmdInvoke
    public static void open(CommandContext co, @CmdPar("what to open") Item item)
    {
        open(co, item, 1);
    }

    @CmdInvoke
    public static void open(CommandContext co, @CmdPar("what to open") Item item, @CmdPar(value = "how many to open", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.assertTrue(item instanceof IOpenable, "*This item cannot be opened.*");

        var openable = (IOpenable) item;

        final long limit = 64;
        CommandAssert.numberInBoundsInclusiveL(amount, 1, limit, "*You can open **%d %s** at most at once and one at least (obviously).*".formatted(limit, item.inlineDescription()));

        CommandAssert.numberNotBelowL(co.ui.howManyOf(item), amount, "You don't have " + amount + " " + item.inlineDescription() + "s...");

        if (openable.requiresKey())
        {
            ItemAssert.consumeItems(co.ui, "open **%d %s**".formatted(amount, item.inlineDescription()), ItemPair.of(Items.keys, amount));

            if (Curser.isBlessed(co, EnumBlessing.CHANCE_NOT_TO_CONSUME_KEY))
            {
                long keysBack = 0;

                for (int i = 0; i < amount; i++)
                    if (BotdirilRnd.rollChance(0.25))
                        keysBack++;

                co.ui.addKeys(keysBack);
            }
        }

        openable.open(co, amount);
        co.ui.addItem(item, -amount);
    }
}
