package com.botdiril.command.superuser;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;

@Command(value = "fid", category = CommandCategory.SUPERUSER, description = "Shows your user ID.")
public class CommandFID
{
    @CmdInvoke
    public static void show(CommandContext co)
    {
        co.respondf("Your FID: **%d**", co.inventory.getFID());
    }

    @CmdInvoke
    public static void show(DiscordCommandContext co, @CmdPar("player") EntityPlayer player)
    {
        var tgtUI = player.inventory();
        co.respondf("%s's FID: **%d**", player.getMention(), tgtUI.getFID());
    }
}
