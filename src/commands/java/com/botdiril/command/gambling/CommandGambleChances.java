package com.botdiril.command.gambling;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.gamelogic.gamble.EnumGambleOutcome;

import java.text.DecimalFormat;

@Command("odds")
public class CommandGambleChances
{
    @CmdInvoke
    public static void print(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setTitle("Gambling odds");
        eb.setDescription("These odds are the same for every user and do not change.");
        eb.setColor(0x008080);

        if (co instanceof DiscordCommandContext dcc)
        {
            eb.setAuthor("%s#%s".formatted(dcc.caller.getName(), dcc.caller.getDiscriminator()));
            eb.setThumbnail(dcc.caller.getEffectiveAvatarUrl());
        }
        else
        {
            eb.setThumbnail(co.botIconURL);
        }

        DecimalFormat numberFormat = new DecimalFormat("0.###");
        for (EnumGambleOutcome go : EnumGambleOutcome.values())
        {
            eb.addField(go.getShortName(), String.format("%s%%", numberFormat.format( go.getActualWeight() * 100)), false);
        }

        co.respond(eb);
    }
}
