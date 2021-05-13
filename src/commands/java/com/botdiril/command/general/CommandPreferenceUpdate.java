package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;
import net.dv8tion.jda.api.EmbedBuilder;

@Command(value = "preferenceupdate", aliases = { "optionupdate", "optupdate", "prefupdate", "prefsupdate",
        "preferenceset", "optset", "prefset", "setpreference", "setopt", "setoption", "setpref", "updatepreference",
        "updateopt", "updatepref",
        "updateoption" }, category = CommandCategory.GENERAL, description = "Update your user preferences.")
public class CommandPreferenceUpdate
{
    @CmdInvoke
    public static void update(CommandContext co, @CmdPar("option ID") EnumUserPreference option, @CmdPar("on/off") boolean enable)
    {
        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());

        if (enable)
        {
            UserPreferences.setBit(co.po, option);
        }
        else
        {
            UserPreferences.clearBit(co.po, option);
        }

        eb.setTitle("Preference updated");
        var indicator = enable ? "on" : "off";

        eb.setDescription(String.format("Succesfully set `%s` to `%s`.", option.getLocalizedName(), indicator));

        co.respond(eb);
    }
}
