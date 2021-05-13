package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.userdata.item.Item;
import net.dv8tion.jda.api.entities.Member;

import com.botdiril.userdata.UserInventory;

@Command(value = "resetitem", category = CommandCategory.SUPERUSER, description = "Resets the amount of an item to 0.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandResetAmount
{
    @CmdInvoke
    public static void resetItem(CommandContext co, @CmdPar("item") Item item)
    {
        co.ui.setItem(item, 0);

        co.respond(String.format("Reset **%s** to **0**!", item.inlineDescription()));
    }

    @CmdInvoke
    public static void resetItem(CommandContext co, @CmdPar("user") Member user, @CmdPar("item") Item item)
    {
        new UserInventory(co.db, user.getUser().getIdLong()).setItem(item, 0);

        co.respond(String.format("Reset **%s's** **%s** to **0**!", user.getEffectiveName(), item.inlineDescription()));
    }
}
