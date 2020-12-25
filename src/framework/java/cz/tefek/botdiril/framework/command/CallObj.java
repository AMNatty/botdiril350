package cz.tefek.botdiril.framework.command;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;

import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.serverdata.ServerConfig;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.properties.PropertyObject;

public class CallObj
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
        this.textChannel.sendMessage(msg).queue();
    }

    public void respond(MessageEmbed msg)
    {
        this.textChannel.sendMessage(msg).queue();
    }

    public void send(TextChannel tc, String msg)
    {
        tc.sendMessage(msg).queue();
    }

    public void send(TextChannel tc, MessageEmbed msg)
    {
        tc.sendMessage(msg).queue();
    }
}
