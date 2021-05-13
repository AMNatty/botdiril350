package com.botdiril.userdata.icon;

import com.botdiril.userdata.IIdentifiable;
import net.dv8tion.jda.api.JDA;

import java.util.regex.Pattern;

public class IconUtil
{
    private static final Pattern ID_PATTERN = Pattern.compile("[0-9]+");

    public static String urlFromIcon(JDA jda, IIdentifiable item)
    {
        var emID = ID_PATTERN.matcher(item.getIcon());

        if (emID.find())
        {
            var emote = jda.getEmoteById(Long.parseLong(emID.group()));

            if (emote != null)
            {
                return emote.getImageUrl();
            }
        }

        return null;
    }
}
