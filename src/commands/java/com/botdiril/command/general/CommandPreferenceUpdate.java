package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;

@Command(value = "preferenceupdate", aliases = { "optionupdate", "optupdate", "prefupdate", "prefsupdate",
        "preferenceset", "optset", "prefset", "setpreference", "setopt", "setoption", "setpref", "updatepreference",
        "updateopt", "updatepref",
        "updateoption" }, category = CommandCategory.GENERAL, description = "Update your user preferences.")
public class CommandPreferenceUpdate
{
    @CmdInvoke
    public static void update(CommandContext co, @CmdPar("option ID") EnumUserPreference option, @CmdPar("on/off") boolean enable)
    {
        var eb = new ResponseEmbed();
        eb.setColor(0x008080);

        if (co instanceof DiscordCommandContext dcc)
        {
            eb.setThumbnail(dcc.caller.getEffectiveAvatarUrl());
            eb.setAuthor(dcc.caller.getAsTag(), null, dcc.caller.getEffectiveAvatarUrl());
        }

        if (enable)
            UserPreferences.setBit(co.userProperties, option);
        else
            UserPreferences.clearBit(co.userProperties, option);

        eb.setTitle("Preference updated");
        var indicator = enable ? "on" : "off";

        eb.setDescription(String.format("Succesfully set `%s` to `%s`.", option.getLocalizedName(), indicator));

        co.respond(eb);
    }
}
