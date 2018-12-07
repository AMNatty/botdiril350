package cz.tefek.botdiril.framework.command;

import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.SelfUser;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Wrapper data structure holding contextual information about a certain command
 * invocation.
 */
public class CallObj
{
    /**
     * The command target user. Can be null.
     */
    public User target;

    /**
     * The command caller, mapped to a member. Can be null.
     */
    public Member targetMember;

    /**
     * The command caller.
     */
    public User caller;

    /**
     * The command caller, mapped to the member.
     */
    public Member callerMember;

    /**
     * The text channel the command was inovked in.
     */
    public TextChannel textChannel;

    /**
     * The guild the command was invoked in.
     */
    public Guild guild;

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
    public CallObj()
    {
    }

    /**
     * Copy the call structure and spoof the target user.
     */
    public CallObj(CallObj co, User user)
    {
        this.caller = co.caller;
        this.callerMember = co.callerMember;
        this.textChannel = co.textChannel;
        this.guild = co.guild;
        this.jda = co.jda;
        this.message = co.message;
        this.bot = co.bot;
        this.messageContentsDisplay = co.messageContentsDisplay;
        this.messageContentsRaw = co.messageContentsRaw;
        this.target = user;
        this.targetMember = this.guild.getMember(this.target);
    }
}
