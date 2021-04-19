package cz.tefek.botdiril.command.general;

import java.util.HashMap;
import java.util.Map;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.util.BotdirilLog;
import cz.tefek.botdiril.userdata.properties.PropertyObject;

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
    public static void bind(CommandContext co, @CmdPar("alias name") String source, @CmdPar("alias replacement") String replacement)
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
            setSlot(co.po, slot, source, replacement);
            co.respond("Alias bound to slot " + slot + ".");
        }
    }

    static int findEmptySlot(CommandContext co)
    {
        var bound = co.po.getUsedAliases();

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
