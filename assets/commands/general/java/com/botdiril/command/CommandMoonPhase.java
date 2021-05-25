package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.gamelogic.woodcut.EnumMoonPhase;

@Command("moonphase")
public class CommandMoonPhase
{
    @CmdInvoke
    public static void moonPhase(CommandContext co)
    {
        co.respondf("**%s**", EnumMoonPhase.current().getName());
    }
}
