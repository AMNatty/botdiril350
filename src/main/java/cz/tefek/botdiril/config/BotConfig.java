package cz.tefek.botdiril.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.framework.command.context.CommandContext;

/**
 * Structure class holding the basic information needed to run the bot. Contains
 * a convenient factory method to load from a JSON file.
 */
public class BotConfig
{
    private static final String configFile = "settings.json";

    /**
     * Factory method. Loads bot's configuration from a file specified in the
     * configFile static final field.
     * 
     * @return A an instance of {@link BotConfig} or <code>null</code> if the load
     *         fails.
     */
    public static BotConfig load() throws IOException
    {
        var file = new File(configFile);

        /*
         * Generate a config file with the default values if it doesn't exist.
         * */
        if (!(file.exists() && !file.isDirectory()))
        {
            // Default config, let the user modify it to their needs
            var cfg = new BotConfig();
            cfg.key = "insert Discord bot key here";
            cfg.messageQueueBounds = 16384;
            cfg.port = 49306;
            cfg.clientId = -1;
            cfg.clientSecret = "insert Discord app client secret";
            cfg.globalPrefix = "botdiril.";

            // Create a pretty-printed JSON            
            var gson = new GsonBuilder().setPrettyPrinting().create();
            var jsonOut = gson.toJson(cfg);

            try (PrintWriter out = new PrintWriter(configFile))
            {
                out.println(jsonOut);
            }

            BotMain.logger.error("You need to set up the settings.json file I've just created.");
            BotMain.logger.error("It's just some basic stuff like the API key.");

            return null;
        }

        var strJson = Files.readString(file.toPath());

        var gson = new Gson();
        var cfg = gson.fromJson(strJson, BotConfig.class);
        return cfg;
    }

    /** The secret key to run Botdiril. */
    private String key;

    /** The limit of how many {@link CommandContext}s can a message queue take. */
    private int messageQueueBounds;

    /** The OAuth2 client secret. */
    private String clientSecret;

    /** Botdiril's client ID. */
    private long clientId;

    /** Web server port. */
    private int port;

    /** Global command prefix. */
    private String globalPrefix;

    /**
     * Gets the Discord client ID.
     * 
     * @return The Discord client ID
     */
    public long getClientId()
    {
        return this.clientId;
    }

    /**
     * Gets the OAuth2 client secret.
     * 
     * @return The OAuth2 client secret
     */
    public String getClientSecret()
    {
        return this.clientSecret;
    }

    /**
     * Gets the global command prefix.
     * 
     * @return The prefix
     */
    public String getGlobalPrefix()
    {
        return this.globalPrefix;
    }

    /**
     * Gets the Discord bot key.
     * 
     * @return The key
     */
    public String getKey()
    {
        return this.key;
    }

    /**
     * The limit of how many {@link CommandContext}s can a message queue take.
     * 
     * @returns The bounds
     */
    public int getMessageQueueBounds()
    {
        return this.messageQueueBounds;
    }

    /**
     * Gets the web server port.
     * 
     * @return The port number
     */
    public int getPort()
    {
        return this.port;
    }
}
