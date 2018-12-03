package cz.tefek.botdiril;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;

import java.io.IOException;

import javax.security.auth.login.LoginException;

import cz.tefek.botdiril.config.BotConfig;
import cz.tefek.botdiril.listener.MainListener;

public class Botdiril
{
    private BotConfig config;
    private MainListener listener;

    public Botdiril() throws IOException
    {
        this.config = BotConfig.load();

        if (this.config == null)
        {
            throw new RuntimeException("Config was not loaded.");
        }
    }

    public void start() throws LoginException, IllegalArgumentException
    {
        this.listener = new MainListener();

        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setToken(this.config.getKey());
        builder.addEventListeners(this.listener);
        builder.build();
    }

    public BotConfig getConfig()
    {
        return config;
    }
}
