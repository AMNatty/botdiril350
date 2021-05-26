package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.preferences.EnumUserPreference;
import com.botdiril.userdata.preferences.UserPreferences;

import java.util.Arrays;
import java.util.Locale;

@Command("preferences")
public class CommandPreferences
{
    @CmdInvoke
    public static void list(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setColor(0x008080);
        eb.setTitle("Preferences");
        eb.setThumbnail(co.player.getAvatarURL());
        eb.setAuthor(co.player.getTag(), null, co.player.getAvatarURL());

        if (co instanceof ChatCommandContext ccc)
        {
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
