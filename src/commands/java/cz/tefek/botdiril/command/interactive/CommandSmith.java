package cz.tefek.botdiril.command.interactive;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.items.pickaxe.ItemPickaxe;
import cz.tefek.botdiril.userdata.items.Items;

import static cz.tefek.botdiril.command.interactive.CommandSmith.SMITH_CONVERSION;

@Command(value = "smith", category = CommandCategory.INTERACTIVE,
    description = "Smith " + SMITH_CONVERSION + " pickaxes of a lower tier to a single pickaxe of a higher tier. Consumes one " + Icons.MINE_STRANGE_METAL + ".",
    levelLock = 15)
public class CommandSmith
{
    public static final int SMITH_CONVERSION = 10;

    @CmdInvoke
    public static void smith(CallObj co, @CmdPar("pickaxe to smith") Item item)
    {
        CommandAssert.assertTrue(item instanceof ItemPickaxe, "That's not a valid pickaxe.");

        var pick = (ItemPickaxe) item;
        var prevPick = pick.getPreviousPickaxe();

        CommandAssert.assertNotNull(prevPick, "That pickaxe cannot be smithed.");

        CommandAssert.numberMoreThanZeroL(co.ui.howManyOf(Items.strangeMetal), String.format("**You need %s to do this.**.", Items.strangeMetal.inlineDescription()));

        var prevCount = co.ui.howManyOf(prevPick);
        CommandAssert.numberNotBelowL(prevCount, SMITH_CONVERSION, String.format("**You need %d more %s to do this.**.", SMITH_CONVERSION - prevCount, prevPick.inlineDescription()));

        co.ui.addItem(Items.strangeMetal, -1);
        co.ui.addItem(prevPick, -SMITH_CONVERSION);
        co.ui.addItem(pick, 1);

        co.respond(String.format("You crafted **a %s** from **%d %s** and **one %s**",
            pick.inlineDescription(),
            SMITH_CONVERSION, prevPick.inlineDescription(),
            Items.strangeMetal.inlineDescription()));
    }
}
