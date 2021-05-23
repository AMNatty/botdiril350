package com.botdiril.command;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;

@Command("setjackpot")
public class CommandSetJackpot
{
    @CmdInvoke
    public static void setJackpot(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("jackpot pool") long pool, @CmdPar("jackpot stored") long stored)
    {
        var po = player.inventory().getPropertyObject();
        po.setJackpot(pool, stored);

        co.respond("**%s**'s jackpot set to **%d**/**%d**.".formatted(player.getMention(), pool, stored));
    }
}
