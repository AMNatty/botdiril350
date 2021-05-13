package com.botdiril.framework.util;

import net.dv8tion.jda.api.entities.Guild;
import org.apache.commons.lang3.StringUtils;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.internal.BotdirilConfig;

public class PrefixUtil
{
    public static boolean findPrefix(Guild g, CommandContext obj)
    {
        var prefix = obj.sc.getPrefix();

        if (StringUtils.startsWithIgnoreCase(obj.contents, prefix))
        {
            obj.contents = StringUtils.removeStartIgnoreCase(obj.contents, prefix);
            obj.usedPrefix = prefix;

            return true;
        }

        if (StringUtils.startsWithIgnoreCase(obj.contents, BotdirilConfig.UNIVERSAL_PREFIX))
        {
            obj.contents = StringUtils.removeStartIgnoreCase(obj.contents, BotdirilConfig.UNIVERSAL_PREFIX);
            obj.usedPrefix = BotdirilConfig.UNIVERSAL_PREFIX;

            return true;
        }

        return false;
    }
}
