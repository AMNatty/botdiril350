package cz.tefek.botdiril.framework.command.invoke;

public class CommandException extends RuntimeException
{
    public CommandException(String reason)
    {
        super(reason);
    }

    public CommandException(String message, Throwable cause)
    {
        super(message, cause);
    }
}
