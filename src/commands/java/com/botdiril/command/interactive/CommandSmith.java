package com.botdiril.command.interactive;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemAssert;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.items.pickaxe.ItemPickaxe;

import static com.botdiril.command.interactive.CommandSmith.SMITH_CONVERSION;

@Command(value = "smith", category = CommandCategory.INTERACTIVE,
    description = "Smith " + SMITH_CONVERSION + " pickaxes of a lower tier to a single pickaxe of a higher tier. Consumes one strange metal.",
    levelLock = 15)
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

        ItemAssert.consumeItems(co.ui, "smith this pickaxe", ItemPair.of(Items.strangeMetal), ItemPair.of(prevPick, SMITH_CONVERSION));

        co.ui.addItem(pick, 1);

        co.respond(String.format("You crafted **a %s** from **%d %s** and **one %s**",
            pick.inlineDescription(),
            SMITH_CONVERSION, prevPick.inlineDescription(),
            Items.strangeMetal.inlineDescription()));
    }
}
