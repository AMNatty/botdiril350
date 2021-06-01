package com.botdiril.command;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;

import java.util.Arrays;

@Command("cleanse")
public class CommandCleanse
{
    @CmdInvoke
    public static void cleanse(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        var userProperties = player.inventory().getPropertyObject();
        Arrays.stream(EnumBlessing.values()).forEach(blessing -> Curser.clear(userProperties, blessing));
        Arrays.stream(EnumCurse.values()).forEach(curse -> Curser.clear(userProperties, curse));

        co.respondf("*Removed all curses and blessings from %s.*", player.getMention());
    }

    @CmdInvoke
    public static void cleanse(CommandContext co)
    {
        cleanse(co, co.player);
    }
}
