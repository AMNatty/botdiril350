package com.botdiril.command.general;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.timers.EnumTimer;

import java.util.Arrays;

import cz.tefek.pluto.chrono.MiniTime;

@Command("timers")
public class CommandTimers
{
    @CmdInvoke
    public static void check(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        var eb = new ResponseEmbed();
        eb.setTitle("Timers");
        eb.setDescription(player.getMention() + "'s active timers / cooldowns.");
        eb.setColor(0x008080);
        eb.setThumbnail(player.getAvatarURL());

        var ui = player.inventory();

        Arrays.stream(EnumTimer.values()).forEach(t ->
        {
            var remaining = ui.checkTimer(t);

            if (remaining < 0)
                return;

            eb.addField(t.getLocalizedName(), MiniTime.formatDiff(remaining), false);
        });

        co.respond(eb);
    }

    @CmdInvoke
    public static void checkSelf(DiscordCommandContext co)
    {
        check(co, co.player);
    }
}
