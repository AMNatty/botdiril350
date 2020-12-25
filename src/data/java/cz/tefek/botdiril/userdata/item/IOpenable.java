package cz.tefek.botdiril.userdata.item;

import cz.tefek.botdiril.framework.command.CallObj;

public interface IOpenable
{
    void open(CallObj co, long amount);

    default boolean requiresKey()
    {
        return false;
    }
}
