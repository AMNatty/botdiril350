package cz.tefek.botdiril.framework.command.invoke;

import net.dv8tion.jda.api.entities.MessageEmbed;

public class CommandException extends RuntimeException
{
    private final boolean isEmbedded;
    private final MessageEmbed embed;

    public CommandException(String reason)
    {
        super(reason);
        this.isEmbedded = false;
        this.embed = null;
    }

    public CommandException(MessageEmbed reason)
    {
        this.isEmbedded = true;
        this.embed = reason;
    }

    public CommandException(String message, Throwable cause)
    {
        super(message, cause);
        this.isEmbedded = false;
        this.embed = null;
    }

    public boolean isEmbedded()
    {
        return this.isEmbedded;
    }

    public MessageEmbed getEmbed()
    {
        return this.embed;
    }
}
