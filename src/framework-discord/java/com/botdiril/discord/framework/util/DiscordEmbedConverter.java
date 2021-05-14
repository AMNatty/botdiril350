package com.botdiril.discord.framework.util;

import com.botdiril.framework.command.MessageOutputTransformer;
import com.botdiril.framework.response.ResponseEmbed;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class DiscordEmbedConverter
{
    @Contract(value = "null -> null", pure = true)
    public static MessageEmbed createEmbed(@Nullable ResponseEmbed embed)
    {
        if (embed == null)
            return null;

        var eb = new EmbedBuilder();

        eb.setTitle(MessageOutputTransformer.transformMessage(embed.getTitle()), embed.getURL());
        eb.setAuthor(embed.getAuthorText(), embed.getAuthorURL(), embed.getAuthorIconURL());
        eb.setFooter(MessageOutputTransformer.transformMessage(embed.getFooterText()), embed.getFooterIconURL());
        eb.setTimestamp(embed.getFooterTimestamp());
        eb.setDescription(MessageOutputTransformer.transformMessage(embed.getDescription()));
        embed.forEachField(field ->
            eb.addField(MessageOutputTransformer.transformMessage(field.name()), MessageOutputTransformer.transformMessage(field.value()), field.inline())
        );

        eb.setThumbnail(embed.getThumbnailURL());
        eb.setImage(embed.getImageURL());

        var color = embed.getColor();
        if (color != null)
            eb.setColor(color);

        return eb.build();
    }
}
