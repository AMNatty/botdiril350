package com.botdiril.discord.framework.response;

import com.botdiril.discord.framework.util.DiscordEmbedConverter;
import com.botdiril.framework.command.MessageOutputTransformer;
import com.botdiril.framework.response.AbstractChatResponse;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;

public class DiscordMessageResponse extends AbstractChatResponse
{
    private final MessageChannel channel;
    private MessageEmbed discordEmbed;

    public DiscordMessageResponse(MessageChannel channel)
    {
        this.channel = channel;
    }

    public DiscordMessageResponse setEmbed(MessageEmbed embed)
    {
        this.discordEmbed = embed;
        this.embed = null;

        return this;
    }

    @Override
    public void send()
    {
        var embed = this.discordEmbed != null ? this.discordEmbed : DiscordEmbedConverter.createEmbed(this.embed);
        var msg = this.message.toString();

        if (!msg.isBlank() && embed != null)
            this.channel.sendMessage(MessageOutputTransformer.transformMessage(msg)).embed(embed).queue();
        else if (!msg.isBlank())
            this.channel.sendMessage(MessageOutputTransformer.transformMessage(msg)).queue();
        else if (embed != null)
            this.channel.sendMessage(embed).queue();
    }
}
