package com.botdiril.discord.framework.command.context;

import com.botdiril.discord.framework.response.DiscordMessageResponse;
import com.botdiril.framework.command.MessageOutputTransformer;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.serverdata.ServerConfig;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

public class DiscordCommandContext extends ChatCommandContext
{
    public ServerConfig sc;

    public User caller;
    public Member callerMember;
    public TextChannel textChannel;
    public Guild guild;
    public JDA jda;
    public Message message;
    public SelfUser bot;

    public DiscordCommandContext(TextChannel textChannel)
    {
        this.textChannel = textChannel;
        this.response = new DiscordMessageResponse(this.textChannel, this.message);
    }

    @Override
    public DiscordMessageResponse createResponse()
    {
        return new DiscordMessageResponse(this.textChannel);
    }

    public DiscordMessageResponse createResponse(MessageChannel channel)
    {
        return new DiscordMessageResponse(channel);
    }

    public DiscordMessageResponse createResponse(MessageChannel channel, Message message)
    {
        return new DiscordMessageResponse(channel, message);
    }

    @Override
    public DiscordMessageResponse getDefaultResponse()
    {
        return (DiscordMessageResponse) this.response;
    }

    public void respond(EmbedBuilder msg)
    {
        transformEmbed(msg);
        this.textChannel.sendMessage(msg.build()).queue();
    }

    private void transformEmbed(EmbedBuilder msg)
    {
        var fakeEmbed = msg.build();

        msg.setDescription(MessageOutputTransformer.transformMessage(fakeEmbed.getDescription()));
        msg.setTitle(MessageOutputTransformer.transformMessage(fakeEmbed.getTitle()));

        msg.clearFields();
        fakeEmbed.getFields().forEach(field -> msg.addField(MessageOutputTransformer.transformMessage(field.getName()),
            MessageOutputTransformer.transformMessage(field.getValue()),
            field.isInline()));

        var authorInfo = fakeEmbed.getAuthor();
        if (authorInfo != null)
            msg.setAuthor(MessageOutputTransformer.transformMessage(authorInfo.getName()), authorInfo.getUrl(), authorInfo.getIconUrl());

        var footerInfo = fakeEmbed.getFooter();
        if (footerInfo != null)
            msg.setFooter(MessageOutputTransformer.transformMessage(footerInfo.getText()));
    }
}
