package cz.tefek.botdiril.command.interactive;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.item.IOpenable;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumBlessing;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(value = "open", aliases = {
        "use" }, category = CommandCategory.INTERACTIVE, description = "Open a card pack/crate/something else.")
public class CommandOpen
{
    @CmdInvoke
    public static void open(CallObj co, @CmdPar("what to open") Item item)
    {
        open(co, item, 1);
    }

    @CmdInvoke
    public static void open(CallObj co, @CmdPar("what to open") Item item, @CmdPar(value = "how many to open", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.assertTrue(item instanceof IOpenable, "This item cannot be opened.");

        var openable = (IOpenable) item;

        CommandAssert.numberInBoundsInclusiveL(amount, 1, 32, "You can open 32 crates at most and one at least (obviously).");

        CommandAssert.numberNotBelowL(co.ui.howManyOf(item), amount, "You don't have " + amount + " " + item.inlineDescription() + "s...");

        if (openable.requiresKey())
        {
            var keys = co.ui.getKeys();
            CommandAssert.numberNotBelowL(keys, amount, String.format("You don't have enough %s for this.", Icons.KEY));
        }

        openable.open(co, amount);
        co.ui.addItem(item, -amount);

        if (openable.requiresKey())
        {
            for (int i = 0; i < amount; i++)
            {
                if (!Curser.isBlessed(co, EnumBlessing.CHANCE_NOT_TO_CONSUME_KEY) || BotdirilRnd.rollChance(0.75))
                {
                    co.ui.addKeys(-1);
                }
            }
        }
    }
}
