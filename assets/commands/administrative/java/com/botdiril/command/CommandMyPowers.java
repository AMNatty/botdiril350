package com.botdiril.command;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.PowerLevel;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;

import java.time.Instant;

@Command("powers")
public class CommandMyPowers
{
    @CmdInvoke
    public static void print(DiscordCommandContext co)
    {
        print(co, co.callerMember);
    }

    @CmdInvoke
    public static void print(DiscordCommandContext co, @CmdPar("user") Member user)
    {
        if (user.getUser().isBot())
        {
            co.respond("This command can't be used on bots.");
            return;
        }

        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setTitle("Power Listing");
        eb.setDescription(user.getAsMention() + "'s powers:");
        eb.setThumbnail(user.getUser().getEffectiveAvatarUrl());
        eb.setFooter("User ID: " + user.getUser().getIdLong(), null);
        eb.setTimestamp(Instant.now());

        PowerLevel.getCumulativePowers(co.db, user, co.textChannel).forEach(c -> eb.addField(c.toString(), c.getDescription(), false));

        co.respond(eb);
    }
}
