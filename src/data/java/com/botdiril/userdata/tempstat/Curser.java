package com.botdiril.userdata.tempstat;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.util.BotdirilRnd;

import cz.tefek.pluto.chrono.MiniTime;

public class Curser
{
    public static void bless(CommandContext co)
    {
        bless(co, co.player);
    }

    public static void curse(CommandContext co)
    {
        curse(co, co.player);
    }

    public static void bless(CommandContext co, EntityPlayer player)
    {
        var blessings = EnumBlessing.values();
        var blessing = BotdirilRnd.choose(blessings);

        bless(co, player, blessing);
    }

    public static void curse(CommandContext co, EntityPlayer player)
    {
        var curses = EnumCurse.values();
        var curse = BotdirilRnd.choose(curses);

        curse(co, player, curse);
    }

    public static void bless(CommandContext co, EntityPlayer player, EnumBlessing blessing)
    {
        var millis = blessing.getDurationInSeconds() * 1000;
        var strTime = MiniTime.formatDiff(millis);

        if (isBlessed(co, blessing))
        {
            co.userProperties.extendBlessing(blessing, millis);
        }
        else
        {
            co.userProperties.setBlessing(blessing, System.currentTimeMillis() + millis);
        }

        var who = player.equals(co.player) ? "You've" : "%s has".formatted(player.getMention());
        co.respondf("%s been **blessed** with the **%s** for **%s**. **%s**", who, blessing.getLocalizedName(), strTime, blessing.getDescription());
    }

    public static void curse(CommandContext co, EntityPlayer player, EnumCurse curse)
    {
        if (isBlessed(co, EnumBlessing.CANT_BE_CURSED))
        {
            var who = player.equals(co.player) ? "Your" : "%s's".formatted(player.getMention());
            co.respondf("***%s %s %s protected you from the %s.***", who, Icons.SCROLL_UNIQUE, EnumBlessing.CANT_BE_CURSED.getLocalizedName(), curse.getLocalizedName());
            return;
        }

        var millis = curse.getDurationInSeconds() * 1000;
        var strTime = MiniTime.formatDiff(millis);

        if (isCursed(co, curse))
        {
            co.userProperties.extendCurse(curse, millis);
        }
        else
        {
            co.userProperties.setCurse(curse, System.currentTimeMillis() + millis);
        }

        var who = player.equals(co.player) ? "You've" : "%s has".formatted(player.getMention());
        co.respondf("%s been **cursed** with the **%s** for **%s**. **%s**", who, curse.getLocalizedName(), strTime, curse.getDescription());
    }

    public static boolean isBlessed(CommandContext co, EnumBlessing blessing)
    {
        return isBlessed(co.userProperties, blessing);
    }

    public static boolean isBlessed(PropertyObject po, EnumBlessing blessing)
    {
        return po.getBlessing(blessing) > System.currentTimeMillis();
    }

    public static boolean isCursed(CommandContext co, EnumCurse curse)
    {
        return isCursed(co.userProperties, curse);
    }

    public static boolean isCursed(PropertyObject po, EnumCurse curse)
    {
        return po.getCurse(curse) > System.currentTimeMillis();
    }
}
