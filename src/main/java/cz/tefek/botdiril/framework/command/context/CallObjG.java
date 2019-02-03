package cz.tefek.botdiril.framework.command.context;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Wrapper data structure holding contextual information about a certain command
 * invocation <b>in a guild</b>.
 */
public class CallObjG extends CallObj
{

    /**
     * The command caller, mapped to a member. Can be null.
     */
    public Member targetMember;

    /**
     * The command caller, mapped to the member.
     */
    public Member callerMember;

    /**
     * The text channel the command was invoked in.
     */
    public TextChannel textChannel;

    /**
     * The guild the command was invoked in.
     */
    public Guild guild;

    /**
     * Create an empty call structure.
     */
    public CallObjG()
    {
    }

    /**
     * Copy the call structure and spoof the target user.
     */
    public CallObjG(CallObjG co, User user)
    {
        super(co, user);

        this.callerMember = co.callerMember;
        this.textChannel = co.textChannel;
        this.target = user;
        this.targetMember = this.guild.getMember(this.target);
    }
}
