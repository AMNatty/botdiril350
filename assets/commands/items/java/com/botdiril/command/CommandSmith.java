package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.pickaxe.ItemPickaxe;
import com.botdiril.util.BotdirilFmt;

@Command("smith")
public class CommandSmith
{
    public static final long SMITH_CONVERSION = 8;

    @CmdInvoke
    public static void smith(CommandContext co, @CmdPar("pickaxe to smith") Item item, @CmdPar("amount") int amount)
    {
        CommandAssert.assertTrue(item instanceof ItemPickaxe, "That's not a valid pickaxe.");

        var pick = (ItemPickaxe) item;
        var prevPick = pick.getPreviousPickaxe();

        CommandAssert.assertNotNull(prevPick, "That pickaxe cannot be smithed.");

        long consumedPickaxes = amount * SMITH_CONVERSION;

        ItemAssert.consumeItems(co.inventory, "smith %s".formatted(BotdirilFmt.amountOfMD(amount, pick)), Items.strangeMetal.ofAmount(amount), prevPick.ofAmount(consumedPickaxes));

        co.inventory.addItem(pick, amount);

        co.respondf("You crafted %s from %s and %s.",
            BotdirilFmt.amountOfMD(amount, pick.getInlineDescription()),
            BotdirilFmt.amountOfMD(consumedPickaxes, prevPick.getInlineDescription()),
            BotdirilFmt.amountOfMD(amount, Items.strangeMetal.getInlineDescription()));
    }

    @CmdInvoke
    public static void smith(CommandContext co, @CmdPar("pickaxe to smith") Item item)
    {
        smith(co, item, 1);
    }
}
