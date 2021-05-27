package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.framework.util.UserAlias;

@Command("alias")
public class CommandAlias
{

    @CmdInvoke
    public static void bind(ChatCommandContext co, @CmdPar("alias name") String source, @CmdPar("alias replacement") String replacement)
    {
        source = source.trim();

        CommandAssert.stringNotEmptyOrNull(source, "Alias cannot be empty!");

        CommandAssert.stringNotTooLong(source, UserAlias.ALIAS_IN_MAX_LENGTH, String.format("Alias source shouldn't be longer than %d characters!", UserAlias.ALIAS_IN_MAX_LENGTH));

        if (source.length() < 5)
        {
            throw new CommandException("Alias source shouldn't be shorter than 5 characters!");
        }

        CommandAssert.stringNotTooLong(replacement, UserAlias.ALIAS_OUT_MAX_LENGTH, String.format("Alias replacement shouldn't be longer than %d characters!", UserAlias.ALIAS_OUT_MAX_LENGTH));

        var slot = UserAlias.findEmptySlot(co);

        if (slot == -1)
        {
            co.respond("You have no empty alias slots. Try freeing some with `removealias`.");
        }
        else
        {
            UserAlias.setSlot(co.userProperties, slot, source, replacement);
            co.respondf("Alias bound to slot %d.", slot);
        }
    }

}
