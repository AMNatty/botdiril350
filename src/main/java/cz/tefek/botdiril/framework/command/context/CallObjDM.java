package cz.tefek.botdiril.framework.command.context;

import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.User;

/**
 * Wrapper data structure holding contextual information about a certain command
 * invocation <b>in a private chat</b>.
 */
public class CallObjDM extends CallObj
{
    /**
     * The private channel the command was invoked in.
     */
    public PrivateChannel privateChannel;

    /**
     * Create an empty call structure.
     */
    public CallObjDM()
    {
    }

    /**
     * Copy the call structure and spoof the target user.
     */
    public CallObjDM(CallObjDM co, User user)
    {
        super(co, user);

        this.privateChannel = co.privateChannel;
    }
}
