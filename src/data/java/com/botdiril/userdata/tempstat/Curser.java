package com.botdiril.userdata.tempstat;

import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.util.BotdirilRnd;
import cz.tefek.pluto.chrono.MiniTime;

public class Curser
{
    public static void bless(CommandContext co)
    {
        var blessings = EnumBlessing.values();
        var blessing = BotdirilRnd.choose(blessings);

        var millis = blessing.getDurationInSeconds() * 1000;
        var strTime = MiniTime.formatDiff(millis);

        if (isBlessed(co, blessing))
        {
            co.po.extendBlessing(blessing, millis);
        }
        else
        {
            co.po.setBlessing(blessing, System.currentTimeMillis() + millis);
        }

        co.respond(String.format("You've been **blessed** with the **%s** for **%s**. **%s**", blessing.getLocalizedName(), strTime, blessing.getDescription()));
    }

    public static void curse(CommandContext co)
    {
        var curses = EnumCurse.values();
        var curse = BotdirilRnd.choose(curses);

        if (isBlessed(co, EnumBlessing.CANT_BE_CURSED))
        {
            co.respond(String.format("***Your %s protected you from the %s.***", EnumBlessing.CANT_BE_CURSED.getLocalizedName(), curse.getLocalizedName()));
            return;
        }

        var millis = curse.getDurationInSeconds() * 1000;
        var strTime = MiniTime.formatDiff(millis);

        if (isCursed(co, curse))
        {
            co.po.extendCurse(curse, millis);
        }
        else
        {
            co.po.setCurse(curse, System.currentTimeMillis() + millis);
        }

        co.respond(String.format("You've been **cursed** with the **%s** for **%s**. **%s**", curse.getLocalizedName(), strTime, curse.getDescription()));
    }

    public static boolean isBlessed(CommandContext co, EnumBlessing blessing)
    {
        return isBlessed(co.po, blessing);
    }

    public static boolean isBlessed(PropertyObject po, EnumBlessing blessing)
    {
        return po.getBlessing(blessing) > System.currentTimeMillis();
    }

    public static boolean isCursed(CommandContext co, EnumCurse curse)
    {
        return isCursed(co.po, curse);
    }

    public static boolean isCursed(PropertyObject po, EnumCurse curse)
    {
        return po.getCurse(curse) > System.currentTimeMillis();
    }
}
