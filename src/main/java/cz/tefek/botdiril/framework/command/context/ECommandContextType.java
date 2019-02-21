package cz.tefek.botdiril.framework.command.context;

import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.PrivateChannel;
import net.dv8tion.jda.core.entities.TextChannel;

/**
 * Enum of all possible contexts command can be invoked in.
 */
public enum ECommandContextType
{
    /**
     * For commands invoked in a {@link TextChannel} in a {@link Guild}.
     */
    GUILD,
    /**
     * For commands invoked in a {@link PrivateChannel}.
     */
    PRIVATE;
}
