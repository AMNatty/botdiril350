package com.botdiril.framework.util;

import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.util.BotdirilLog;

import java.util.HashMap;
import java.util.Map;

public class UserAlias
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

    public static int findEmptySlot(CommandContext co)
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

    public static void setSlot(PropertyObject po, int slot, String in, String out)
    {
        var bound = po.getUsedAliases();

        po.setUsedAliases((byte) (bound | 1 << slot));
        po.setAlias(slot, in, out);
    }
}
