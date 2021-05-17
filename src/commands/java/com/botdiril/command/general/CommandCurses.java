package com.botdiril.command.general;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.tempstat.EnumBlessing;
import com.botdiril.userdata.tempstat.EnumCurse;

import java.util.Arrays;

import cz.tefek.pluto.chrono.MiniTime;

@Command("curses")
public class CommandCurses
{
    @CmdInvoke
    public static void check(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        var eb = new ResponseEmbed();
        eb.setAuthor(player.getTag(), null, player.getAvatarURL());
        eb.setTitle("Curse/blessing timers");
        eb.setDescription(player.getMention() + "'s active blessings/curses.");
        eb.setColor(0x008080);
        eb.setThumbnail(player.getAvatarURL());

        var po = player.inventory().getPropertyObject();

        Arrays.stream(EnumCurse.values()).forEach(t ->
        {
            var remaining = po.getCurse(t) - System.currentTimeMillis();

            if (remaining < 0)
                return;

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), true);
        });

        Arrays.stream(EnumBlessing.values()).forEach(t ->
        {
            var remaining = po.getBlessing(t) - System.currentTimeMillis();

            if (remaining < 0)
                return;

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), true);
        });

        co.respond(eb);
    }

    @CmdInvoke
    public static void checkSelf(DiscordCommandContext co)
    {
        check(co, co.player);
    }
}
