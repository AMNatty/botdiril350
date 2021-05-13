package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.userdata.properties.PropertyObject;
import net.dv8tion.jda.api.entities.User;

import com.botdiril.userdata.UserInventory;

@Command(value = "setjackpot",
    description = "Sets a user's jackpot.",
    powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandSetJackpot
{
    @CmdInvoke
    public static void setJackpot(CommandContext co, @CmdPar("targer") User user, @CmdPar("jackpot pool") long pool, @CmdPar("jackpot stored") long stored)
    {
        var ui = new UserInventory(co.db, user.getIdLong());
        var po = new PropertyObject(co.db, ui.getFID());
        po.setJackpot(pool, stored);

        co.respond("%s's jackpot set to **%d**/**%d**.".formatted(user, pool, stored));
    }
}
