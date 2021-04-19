package cz.tefek.botdiril.userdata.item;

import cz.tefek.botdiril.framework.command.CommandContext;

public interface IOpenable
{
    void open(CommandContext co, long amount);

    default boolean requiresKey()
    {
        return false;
    }
}
