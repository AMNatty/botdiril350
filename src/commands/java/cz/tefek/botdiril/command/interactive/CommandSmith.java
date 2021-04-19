package cz.tefek.botdiril.command.interactive;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemAssert;
import cz.tefek.botdiril.userdata.item.ItemPair;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.userdata.items.pickaxe.ItemPickaxe;

import static cz.tefek.botdiril.command.interactive.CommandSmith.SMITH_CONVERSION;

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
