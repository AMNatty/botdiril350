package com.botdiril.framework.command.invoke;

import net.dv8tion.jda.api.EmbedBuilder;

public class CommandException extends RuntimeException
{
    private final boolean isEmbedded;
    private final EmbedBuilder embed;

    public CommandException(String reason)
    {
        super(reason);
        this.isEmbedded = false;
        this.embed = null;
    }

    public CommandException(EmbedBuilder reason)
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

    public EmbedBuilder getEmbed()
    {
        return this.embed;
    }
}
