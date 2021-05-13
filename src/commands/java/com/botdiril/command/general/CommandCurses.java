package com.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.util.Arrays;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.pluto.chrono.MiniTime;

@Command(value = "curses", aliases = {
        "blessings" }, category = CommandCategory.GENERAL, description = "Check user's timers.")
public class CommandCurses
{
    @CmdInvoke
    public static void check(CommandContext co, @CmdPar("user") User user)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor(user.getAsTag(), null, user.getEffectiveAvatarUrl());
        eb.setTitle("Curse/blessing timers");
        eb.setDescription(user.getAsMention() + "'s active blessings/curses.");

        var ui = new UserInventory(co.db, user.getIdLong());

        var po = new PropertyObject(co.db, ui.getFID());

        Arrays.stream(EnumCurse.values()).forEach(t ->
        {
            var remaining = po.getCurse(t) - System.currentTimeMillis();

            if (remaining < 0)
            {
                return;
            }

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), true);
        });

        Arrays.stream(EnumBlessing.values()).forEach(t ->
        {
            var remaining = po.getBlessing(t) - System.currentTimeMillis();

            if (remaining < 0)
            {
                return;
            }

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), true);
        });

        eb.setColor(0x008080);
        eb.setThumbnail(user.getEffectiveAvatarUrl());

        co.respond(eb);
    }

    @CmdInvoke
    public static void checkSelf(CommandContext co)
    {
        check(co, co.caller);
    }
}
