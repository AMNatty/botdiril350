package com.botdiril.command;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.Item;

@Command("additem")
public class CommandAddItem
{
    @CmdInvoke
    public static void addItem(CommandContext co, @CmdPar("item") Item item)
    {
        co.inventory.addItem(item, 1);

        co.respondf("Added **%d %s**!", 1, item.getInlineDescription());
    }

    @CmdInvoke
    public static void addItem(CommandContext co, @CmdPar("item") Item item, @CmdPar("how many to add") long howmany)
    {
        CommandAssert.numberInBoundsInclusiveL(howmany, 0, Integer.MAX_VALUE, "That number is too small/big!");

        co.inventory.addItem(item, howmany);

        co.respondf("Added **%d %s**!", howmany, item.getInlineDescription());
    }

    @CmdInvoke
    public static void addItem(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("item") Item item, @CmdPar("how many to add") long howmany)
    {
        CommandAssert.numberInBoundsInclusiveL(howmany, 0, Integer.MAX_VALUE, "That number is too small/big!");

        player.inventory().addItem(item, howmany);

        co.respondf("Added **%d %s** to %s's inventory!", howmany, item.getInlineDescription(), player.getMention());
    }
}
