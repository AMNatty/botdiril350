package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.item.Item;
import net.dv8tion.jda.api.entities.User;

import com.botdiril.userdata.UserInventory;

@Command(value = "additem", category = CommandCategory.SUPERUSER, description = "Adds items to the target's inventory (for science of course).", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandAddItem
{
    @CmdInvoke
    public static void addItem(CommandContext co, @CmdPar("item") Item item)
    {
        co.ui.addItem(item, 1);

        co.respond(String.format("Added **%d %s**!", 1, item.inlineDescription()));
    }

    @CmdInvoke
    public static void addItem(CommandContext co, @CmdPar("item") Item item, @CmdPar("how many to add") long howmany)
    {
        CommandAssert.numberInBoundsInclusiveL(howmany, 0, Integer.MAX_VALUE, "That number is too small/big!");

        co.ui.addItem(item, howmany);

        co.respond(String.format("Added **%d %s**!", howmany, item.inlineDescription()));
    }

    @CmdInvoke
    public static void addItem(CommandContext co, @CmdPar("user") User user, @CmdPar("item") Item item, @CmdPar("how many to add") long howmany)
    {
        CommandAssert.numberInBoundsInclusiveL(howmany, 0, Integer.MAX_VALUE, "That number is too small/big!");

        new UserInventory(co.db, user.getIdLong()).addItem(item, howmany);

        co.respond(String.format("Added **%d %s** to %s's inventory!", howmany, item.inlineDescription(), user.getAsMention()));
    }
}
