package com.botdiril.discord.framework.response;

import com.botdiril.discord.framework.util.DiscordEmbedConverter;
import com.botdiril.framework.command.MessageOutputTransformer;
import com.botdiril.framework.response.AbstractChatResponse;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class DiscordMessageResponse extends AbstractChatResponse
{
    private final MessageChannel channel;
    private final Message replyMessage;
    private MessageEmbed discordEmbed;

    public DiscordMessageResponse(MessageChannel channel)
    {
        this(channel, null);
    }

    public DiscordMessageResponse(MessageChannel channel, Message message)
    {
        this.channel = channel;
        this.replyMessage = message;
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

        MessageAction msgAction;

        if (!msg.isBlank() && embed != null)
            msgAction = this.channel.sendMessage(MessageOutputTransformer.transformMessage(msg)).embed(embed);
        else if (!msg.isBlank())
            msgAction = this.channel.sendMessage(MessageOutputTransformer.transformMessage(msg));
        else if (embed != null)
            msgAction = this.channel.sendMessage(embed);
        else
            return;

        if (this.replyMessage != null)
            msgAction = msgAction.mentionRepliedUser(false).reference(this.replyMessage);

        msgAction.queue();
    }
}
