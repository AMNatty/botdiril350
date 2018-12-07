package cz.tefek.botdiril.framework;

import java.util.concurrent.LinkedBlockingQueue;

import cz.tefek.botdiril.config.BotConfig;
import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Worker;

/**
 * Used by {@link Worker}s to take and process pending {@link CommandContext}s.
 */
public class MessageQueue extends LinkedBlockingQueue<CommandContext>
{

    /**
     * 
     */
    private static final long serialVersionUID = -5388293090029843685L;

    public MessageQueue(BotConfig config)
    {
        super(config.getMessageQueueBounds());
    }
}
