package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.framework.util.CommandAssert;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.User;

import java.time.Instant;

import com.botdiril.userdata.UserInventory;
import com.botdiril.userdata.xp.XPRewards;

@Command(value = "setlevel", category = CommandCategory.SUPERUSER, aliases = {
        "setlvl" }, description = "Force sets the user's level.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandSetLevel
{
    @CmdInvoke
    public static void setLevel(CommandContext co, @CmdPar("level") int level)
    {
        setLevel(co, co.caller, level);
    }

    @CmdInvoke
    public static void setLevel(CommandContext co, @CmdPar("user") User user, @CmdPar("level") int level)
    {
        CommandAssert.numberInBoundsInclusiveL(level, 0, XPRewards.getMaxLevel(), String.format("Level must be between 0 and %d.", XPRewards.getMaxLevel()));

        var ui = new UserInventory(co.db, user.getIdLong());

        ui.setXP(0);
        ui.setLevel(level);

        var eb = new EmbedBuilder();
        eb.setTitle("Botdiril SuperUser");
        eb.setColor(0x008080);
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());
        eb.setThumbnail(user.getEffectiveAvatarUrl());
        eb.setDescription(String.format("Updated %s's level to **%d**!", user.getAsMention(), level));
        eb.addField("Channel", co.textChannel.getAsMention(), false);
        eb.addField("SuperUser", co.caller.getAsMention(), false);
        eb.setFooter("Message ID: " + co.message.getIdLong(), null);
        eb.setTimestamp(Instant.now());

        co.respond(eb);
    }
}
