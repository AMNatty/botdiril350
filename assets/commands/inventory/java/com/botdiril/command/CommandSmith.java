package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.pickaxe.ItemPickaxe;
import com.botdiril.util.BotdirilFmt;

@Command("smith")
public class CommandSmith
{
    public static final int SMITH_CONVERSION = 10;

    @CmdInvoke
    public static void smith(CommandContext co, @CmdPar("pickaxe to smith") Item item)
    {
        CommandAssert.assertTrue(item instanceof ItemPickaxe, "That's not a valid pickaxe.");

        var pick = (ItemPickaxe) item;
        var prevPick = pick.getPreviousPickaxe();

        CommandAssert.assertNotNull(prevPick, "That pickaxe cannot be smithed.");

        ItemAssert.consumeItems(co.inventory, "smith this pickaxe", ItemPair.of(Items.strangeMetal), ItemPair.of(prevPick, SMITH_CONVERSION));

        co.inventory.addItem(pick, 1);

        co.respondf("You crafted %s from %s and %s.",
            BotdirilFmt.amountOfMD("a", pick.inlineDescription()),
            BotdirilFmt.amountOfMD(SMITH_CONVERSION, prevPick.inlineDescription()),
            BotdirilFmt.amountOfMD("one", Items.strangeMetal.inlineDescription()));
    }
}
