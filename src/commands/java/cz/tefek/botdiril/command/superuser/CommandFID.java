package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.userdata.UserInventory;

@Command(value = "fid", category = CommandCategory.SUPERUSER, description = "Shows your user ID.")
public class CommandFID
{
    @CmdInvoke
    public static void show(CallObj co)
    {
        co.respond("Your FID: **%d**".formatted(co.ui.getFID()));
    }

    @CmdInvoke
    public static void show(CallObj co, @CmdPar("user") User user)
    {
        var tgtUI = new UserInventory(co.db, user.getIdLong());
        co.respond("%s's FID: **%d**".formatted(user.getAsMention(), tgtUI.getFID()));
    }
}
