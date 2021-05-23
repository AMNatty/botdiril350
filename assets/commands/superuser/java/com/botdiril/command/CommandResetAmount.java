package com.botdiril.command;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.item.Item;

@Command("resetitem")
public class CommandResetAmount
{
    @CmdInvoke
    public static void resetItem(CommandContext co, @CmdPar("item") Item item)
    {
        co.inventory.setItem(item, 0);

        co.respondf("Reset **%s** to **0**!", item.inlineDescription());
    }

    @CmdInvoke
    public static void resetItem(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("item") Item item)
    {
        player.inventory().setItem(item, 0);

        co.respondf("Reset **%s's** **%s** to **0**!", player.getName(), item.inlineDescription());
    }
}
