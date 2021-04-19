package cz.tefek.botdiril.command.gambling;

import net.dv8tion.jda.api.EmbedBuilder;

import java.text.DecimalFormat;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.gamelogic.gamble.EnumGambleOutcome;

@Command(value = "odds", category = CommandCategory.GAMBLING, description = "Your gambling odds.", levelLock = 5)
public class CommandGambleChances
{
    @CmdInvoke
    public static void print(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Gambling odds");
        eb.setDescription("These odds are the same for every user and do not change.");
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setColor(0x008080);

        DecimalFormat numberFormat = new DecimalFormat("0.###");
        for (EnumGambleOutcome go : EnumGambleOutcome.values())
        {
            eb.addField(go.getShortName(), String.format("%s%%", numberFormat.format( go.getActualWeight() * 100)), false);
        }

        co.respond(eb);
    }
}
