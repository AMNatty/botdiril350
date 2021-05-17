package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.util.CommandAssert;

@Command("removealias")
public class CommandRemoveAlias
{
    @CmdInvoke
    public static void unbind(ChatCommandContext co, @CmdPar("alias number") int number)
    {
        CommandAssert.numberInBoundsInclusiveL(number, 0, Byte.SIZE - 1, "Alias number must be non-negative and less than " + Byte.SIZE + "!");

        var bound = co.userProperties.getUsedAliases();

        var bit = (byte) (1 << number);

        if ((bound & bit) != 0)
        {
            co.userProperties.setUsedAliases((byte) (bound & ~bit));
            co.respondf("Alias with the number %d was removed.", number);
        }
        else
        {
            co.respondf("Alias with the number %d does not exist and could not be removed.", number);
        }
    }
}
