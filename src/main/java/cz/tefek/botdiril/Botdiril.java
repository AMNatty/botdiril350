package cz.tefek.botdiril;

import java.util.Map;

import net.dv8tion.jda.bot.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.bot.sharding.ShardManager;

import java.io.IOException;
import java.text.MessageFormat;

import cz.tefek.botdiril.config.BotConfig;
import cz.tefek.botdiril.data.config.guild.GuildConfig;
import cz.tefek.botdiril.data.config.guild.GuildConfigMap;
import cz.tefek.botdiril.framework.command.Worker;
import cz.tefek.botdiril.listener.MainListener;
import cz.tefek.botdiril.servlet.ServletController;

/**
 * Botdiril class, holding configuration, workers, shard manager instance,
 * servlet controller and runtime information in general.
 */
public class Botdiril
{
    /**
     * A {@link BotConfig} instance.
     */
    private BotConfig config;

    /**
     * A {@link MainListener} handler instance.
     */
    private MainListener listener;

    /**
     * A {@link ServletController} instance.
     */
    private ServletController servletController;

    /**
     * A {@link ShardManager} instance.
     */
    private ShardManager shardManager;

    /**
     * An array of {@link Worker} instances.
     */
    private Worker[] workers;

    /**
     * A map of all {@link GuildConfig}s.
     */
    private Map<Long, GuildConfig> guildConfigurations;

    /**
     * The constructor, invocation of which starts an attempt to load the
     * configuration file.
     */
    public Botdiril() throws IOException
    {
        this.config = BotConfig.load();

        if (this.config == null)
        {
            throw new RuntimeException("Config was not loaded.");
        }
    }

    /**
     * Returns the {@link BotConfig} instance.
     * 
     * @return An instance of {@link BotConfig}.
     */
    public BotConfig getConfig()
    {
        return this.config;
    }

    /**
     * Get a {@link GuildConfig} from the {@link Map} by snowflake id,
     * create/deserialize when it is not present.
     * 
     */
    public GuildConfig getGuildConfig(long id)
    {
        var gc = this.guildConfigurations.get(id);

        // GuildConfig is not present in the map
        if (gc == null)
        {
            gc = new GuildConfig(id);

            // Add the mapping
            this.guildConfigurations.put(id, gc);
        }

        return gc;
    }

    /**
     * Returns the {@link Map} of {@link GuildConfig}s.
     * 
     * @return the {@link Map} of {@link GuildConfig}s
     */
    public Map<Long, GuildConfig> getGuildConfigurations()
    {
        return this.guildConfigurations;
    }

    /**
     * Returns the {@link ShardManager} instance.
     * 
     * @return An instance of {@link ShardManager}.
     */
    public ShardManager getShardManager()
    {
        return this.shardManager;
    }

    /**
     * Attempts to initialize and start the bot.
     */
    public void start() throws Exception
    {
        this.guildConfigurations = new GuildConfigMap();

        // Approximate amount of threads to create
        var workerCount = Runtime.getRuntime().availableProcessors() + 2;

        this.workers = new Worker[workerCount];

        for (int i = 0; i < this.workers.length; i++)
        {
            this.workers[i] = new Worker();
            BotMain.logger.info(String.format("Initializing worked ID %d/%d.", i + 1, this.workers.length));
        }

        this.listener = new MainListener();

        // Create the ShardManager
        DefaultShardManagerBuilder builder = new DefaultShardManagerBuilder();
        builder.setToken(this.config.getKey());
        builder.addEventListeners(this.listener);
        this.shardManager = builder.build();

        // Print shard count
        BotMain.logger.info(MessageFormat.format("Total {0} shards.", this.shardManager.getShardsTotal()));

        // Create the servlet controller
        this.servletController = new ServletController(this.config);
        this.servletController.start();
    }
}
