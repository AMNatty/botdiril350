package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.properties.PropertyObject;

@Command(value = "setjackpot",
    description = "Sets a user's jackpot.",
    powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandSetJackpot
{
    @CmdInvoke
    public static void setJackpot(CallObj co, @CmdPar("targer") User user, @CmdPar("jackpot pool") long pool, @CmdPar("jackpot stored") long stored)
    {
        var ui = new UserInventory(co.db, user.getIdLong());
        var po = new PropertyObject(co.db, ui.getFID());
        po.setJackpot(pool, stored);

        co.respond("%s's jackpot set to **%d**/**%d**.".formatted(user, pool, stored));
    }
}
