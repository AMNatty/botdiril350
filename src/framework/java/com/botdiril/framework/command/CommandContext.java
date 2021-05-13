package com.botdiril.framework.command;

import com.botdiril.framework.sql.DBConnection;
import com.botdiril.serverdata.ServerConfig;
import com.botdiril.userdata.properties.PropertyObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import com.botdiril.userdata.UserInventory;

public class CommandContext
{
    public User caller;
    public Member callerMember;
    public TextChannel textChannel;
    public Guild guild;
    public JDA jda;
    public Message message;
    public SelfUser bot;

    public ServerConfig sc;

    public String contents;

    public String usedPrefix;
    public String usedAlias;

    public DBConnection db;
    public UserInventory ui;
    public PropertyObject po;

    public void respond(String msg)
    {
        this.textChannel.sendMessage(MessageOutputTransformer.transformMessage(msg)).queue();
    }

    public void respond(EmbedBuilder msg)
    {
        transformEmbed(msg);
        this.textChannel.sendMessage(msg.build()).queue();
    }

    public void send(TextChannel tc, String msg)
    {
        tc.sendMessage(MessageOutputTransformer.transformMessage(msg)).queue();
    }

    public void send(TextChannel tc, EmbedBuilder msg)
    {
        transformEmbed(msg);
        tc.sendMessage(msg.build()).queue();
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
