package com.botdiril.command;

import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.serverdata.ChannelPreferences;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;

import java.time.Instant;

@Command("toggledisable")
public class CommandDisableChannel
{
    @CmdInvoke
    public static void toggleDisable(DiscordCommandContext co)
    {
        toggleDisable(co, co.textChannel);
    }

    @CmdInvoke
    public static void toggleDisable(DiscordCommandContext co, @CmdPar("channel") TextChannel channel)
    {
        var channelId = co.textChannel.getIdLong();
        var on = !ChannelPreferences.checkBit(co.db, channelId, ChannelPreferences.BIT_DISABLED);
        var lc = co.guild.getTextChannelById(co.sc.getLoggingChannel());

        if (on)
        {
            ChannelPreferences.setBit(co.db, channelId, ChannelPreferences.BIT_DISABLED);
            co.respondf("Channel %s is now disabled for non-elevated users.", channel.getAsMention());
        }
        else
        {
            ChannelPreferences.clearBit(co.db, channelId, ChannelPreferences.BIT_DISABLED);
            co.respondf("Channel %s is now enabled for non-elevated users.", channel.getAsMention());
        }

        if (lc != null)
        {
            var eb = new EmbedBuilder();
            eb.setTitle("Botdiril SuperUser");
            eb.setColor(0x008080);

            if (on)
            {
                eb.setDescription("Channel interaction has been disabled for non-elevated users.");
            }
            else
            {
                eb.setDescription("Channel interaction has been enabled for non-elevated users.");
            }

            eb.addField("User", co.caller.getAsMention(), false);
            eb.addField("Channel", co.textChannel.getAsMention(), false);
            eb.setFooter("Message ID: " + co.message.getIdLong(), null);
            eb.setTimestamp(Instant.now());

            co.createResponse(lc).setEmbed(eb.build()).send();
        }
    }
}
