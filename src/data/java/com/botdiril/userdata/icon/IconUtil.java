package com.botdiril.userdata.icon;

import com.botdiril.userdata.IGameObject;
import net.dv8tion.jda.api.JDA;

import java.util.regex.Pattern;

public class IconUtil
{
    private static final Pattern ID_PATTERN = Pattern.compile("[0-9]+");

    public static String urlFromIcon(JDA jda, IGameObject item)
    {
        var iconRaw = item.getIcon();
        var icon = Icons.getOrDefault(iconRaw, iconRaw);
        var emID = ID_PATTERN.matcher(icon);

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
