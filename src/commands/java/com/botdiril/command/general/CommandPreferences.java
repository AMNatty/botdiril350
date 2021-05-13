package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;
import net.dv8tion.jda.api.EmbedBuilder;

import java.util.Arrays;
import java.util.Locale;

@Command(value = "preferences", aliases = { "options",
        "prefs" }, category = CommandCategory.GENERAL, description = "Show your user preferences.")
public class CommandPreferences
{
    @CmdInvoke
    public static void list(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setTitle("Preferences");
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());
        eb.setDescription(String.format("Your Botdiril preferences.\nYou can update an option with `%sprefupdate <option ID> on/off`.", co.usedPrefix));

        Arrays.stream(EnumUserPreference.values()).forEach(pref ->
        {
            var indicator = UserPreferences.isBitEnabled(co.po, pref) ? "on" : "off";
            eb.addField(pref.getLocalizedName(), String.format("**ID:**`%s`\n**Status:** *%s*", pref.toString().toLowerCase(Locale.ROOT), indicator), true);
        });

        co.respond(eb);
    }
}
