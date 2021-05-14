package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.util.BotdirilLog;

import java.util.HashMap;
import java.util.Map;

@Command(value = "alias", category = CommandCategory.GENERAL, description = "Set an alias for some text.")
public class CommandAlias
{
    public static final int ALIAS_IN_MAX_LENGTH = 32;
    public static final int ALIAS_OUT_MAX_LENGTH = 32;

    public static Map<String, String> allAliases(PropertyObject po)
    {
        var map = new HashMap<String, String>();
        var bound = po.getUsedAliases();

        for (int i = 0; i < Byte.SIZE; i++)
        {
            if ((1 << i & bound) > 0)
            {
                try
                {
                    var entry = po.getAlias(i);
                    map.put(entry.getKey(), entry.getValue());
                }
                catch (Exception e)
                {
                    BotdirilLog.logger.error("Non-existent alias accessed.", e);
                }
            }
        }

        return map;
    }

    @CmdInvoke
    public static void bind(ChatCommandContext co, @CmdPar("alias name") String source, @CmdPar("alias replacement") String replacement)
    {
        source = source.trim();

        if (source.isEmpty())
        {
            throw new CommandException("Alias cannot be empty!");
        }

        CommandAssert.stringNotTooLong(source, ALIAS_IN_MAX_LENGTH, String.format("Alias source shouldn't be longer than %d characters!", ALIAS_IN_MAX_LENGTH));

        if (source.length() < 5)
        {
            throw new CommandException("Alias source shouldn't be shorter than 5 characters!");
        }

        CommandAssert.stringNotTooLong(replacement, ALIAS_OUT_MAX_LENGTH, String.format("Alias replacement shouldn't be longer than %d characters!", ALIAS_OUT_MAX_LENGTH));

        var slot = findEmptySlot(co);

        if (slot == -1)
        {
            co.respond("You have no empty alias slots. Try freeing some with `removealias`.");
        }
        else
        {
            setSlot(co.userProperties, slot, source, replacement);
            co.respondf("Alias bound to slot %d.", slot);
        }
    }

    static int findEmptySlot(CommandContext co)
    {
        var bound = co.userProperties.getUsedAliases();

        for (int i = 0; i < Byte.SIZE; i++)
        {
            if ((1 << i & bound) == 0)
            {
                return i;
            }
        }

        return -1;
    }

    static void setSlot(PropertyObject po, int slot, String in, String out)
    {
        var bound = po.getUsedAliases();

        po.setUsedAliases((byte) (bound | 1 << slot));
        po.setAlias(slot, in, out);
    }
}
