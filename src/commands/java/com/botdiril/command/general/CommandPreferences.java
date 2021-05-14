package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;

import java.util.Arrays;
import java.util.Locale;

@Command(value = "preferences", aliases = { "options",
        "prefs" }, category = CommandCategory.GENERAL, description = "Show your user preferences.")
public class CommandPreferences
{
    @CmdInvoke
    public static void list(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setColor(0x008080);
        eb.setTitle("Preferences");

        if (co instanceof ChatCommandContext ccc)
        {
            if (ccc instanceof DiscordCommandContext dcc)
            {
                eb.setThumbnail(dcc.caller.getEffectiveAvatarUrl());
                eb.setAuthor(dcc.caller.getAsTag(), null, dcc.caller.getEffectiveAvatarUrl());
            }

            eb.setDescription(String.format("Your Botdiril preferences.\nYou can update an option with `%sprefupdate <option ID> on/off`.", ccc.usedPrefix));
        }

        Arrays.stream(EnumUserPreference.values()).forEach(pref ->
        {
            var indicator = UserPreferences.isBitEnabled(co.userProperties, pref) ? "on" : "off";
            eb.addField(pref.getLocalizedName(), String.format("**ID:**`%s`\n**Status:** *%s*", pref.toString().toLowerCase(Locale.ROOT), indicator), true);
        });

        co.respond(eb);
    }
}
