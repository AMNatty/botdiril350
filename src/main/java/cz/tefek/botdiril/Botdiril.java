package cz.tefek.botdiril;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;

import java.io.IOException;

import cz.tefek.botdiril.config.BotConfig;
import cz.tefek.botdiril.listener.MainListener;
import cz.tefek.botdiril.servlet.ServletController;

/**
 *  
 * */
public class Botdiril
{
    private BotConfig config;
    private MainListener listener;
    private ServletController servletController;
    private ShardManager shardManager;

    public Botdiril() throws IOException
    {
        this.config = BotConfig.load();

        if (this.config == null)
        {
            throw new RuntimeException("Config was not loaded.");
        }
    }

    public void start() throws Exception
    {
        this.listener = new MainListener();

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setToken(this.config.getKey());
        builder.addEventListeners(this.listener);
        this.shardManager = builder.build();

        this.servletController = new ServletController(this.config);
        this.servletController.start();
    }

    public BotConfig getConfig()
    {
        return config;
    }

    public ShardManager getShardManager()
    {
        return shardManager;
    }
}
