package com.botdiril.command.superuser;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.xp.XPRewards;

import java.time.ZonedDateTime;

@Command(value = "setlevel", category = CommandCategory.SUPERUSER, aliases = {
        "setlvl" }, description = "Force sets the user's level.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandSetLevel
{
    @CmdInvoke
    public static void setLevel(CommandContext co, @CmdPar("level") int level)
    {
        setLevel(co, co.player, level);
    }

    @CmdInvoke
    public static void setLevel(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("level") int level)
    {
        CommandAssert.numberInBoundsInclusiveL(level, 0, XPRewards.getMaxLevel(), String.format("Level must be between 0 and %d.", XPRewards.getMaxLevel()));

        var ui = player.inventory();

        ui.setXP(0);
        ui.setLevel(level);

        var eb = new ResponseEmbed();
        eb.setTitle("Botdiril SuperUser");
        eb.setColor(0x008080);
        eb.setAuthor(co.player.getTag(), null, co.player.getAvatarURL());
        eb.setThumbnail(co.player.getAvatarURL());
        eb.setDescription(String.format("Updated %s's level to **%d**!", player.getMention(), level));
        eb.addField("SuperUser", co.player.getMention(), false);
        eb.setFooterTimestamp(ZonedDateTime.now());

        if (co instanceof DiscordCommandContext dcc)
        {
            eb.addField("Channel", dcc.textChannel.getAsMention(), false);
            eb.setFooter("Message ID: " + dcc.message.getIdLong());
        }


        co.respond(eb);
    }
}
