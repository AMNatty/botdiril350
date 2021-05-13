package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import net.dv8tion.jda.api.entities.User;

import com.botdiril.userdata.UserInventory;

@Command(value = "fid", category = CommandCategory.SUPERUSER, description = "Shows your user ID.")
public class CommandFID
{
    @CmdInvoke
    public static void show(CommandContext co)
    {
        co.respond("Your FID: **%d**".formatted(co.ui.getFID()));
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("user") User user)
    {
        var tgtUI = new UserInventory(co.db, user.getIdLong());
        co.respond("%s's FID: **%d**".formatted(user.getAsMention(), tgtUI.getFID()));
    }
}
