package cz.tefek.botdiril.framework.command.context;

import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.MessageBuilder;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.User;

/**
 * Wrapper data structure holding contextual information about a certain command
 * invocation.
 */
public abstract class CallObj
{
    /**
     * The command target user. Can be null.
     */
    public User target;

    /**
     * The command caller.
     */
    public User caller;

    /**
     * The channel the command was invoked in.
     */
    public MessageChannel channel;

    /**
     * An instance of {@link JDA}.
     */
    public JDA jda;

    /**
     * {@link Message} object of the invoked command.
     */
    public Message message;

    /**
     * The bot {@link User} .
     */
    public SelfUser bot;

    /**
     * Raw message contents with all formatting characters.
     */
    public String messageContentsRaw;

    /**
     * Message contents as seen by the user.
     */
    public String messageContentsDisplay;

    /**
     * Create an empty call structure.
     */
    protected CallObj()
    {
    }

    /**
     * Copy the call structure and spoof the target user.
     */
    protected CallObj(CallObj co, User user)
    {
        this.caller = co.caller;
        this.jda = co.jda;
        this.message = co.message;
        this.bot = co.bot;
        this.messageContentsDisplay = co.messageContentsDisplay;
        this.messageContentsRaw = co.messageContentsRaw;
        this.target = user;
    }

    /**
     * Shorthand to quickly respond with a message.
     */
    public void respond(String text)
    {
        this.channel.sendMessage(text).submit();
    }

    /**
     * Shorthand to quickly respond with an embed message.
     */
    public void respond(EmbedBuilder embedBuilder)
    {
        this.channel.sendMessage(embedBuilder.build()).submit();
    }

    /**
     * Shorthand to quickly respond with an text + embed message.
     */
    public void respond(String text, EmbedBuilder embedBuilder)
    {
        var message = new MessageBuilder();
        message.setContent(text);
        message.setEmbed(embedBuilder.build());

        this.channel.sendMessage(message.build()).submit();
    }
}
