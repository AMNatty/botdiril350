package cz.tefek.botdiril.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import cz.tefek.botdiril.BotMain;

public class BotConfig
{
    /** The secret key to run CuteBot */
    private String key;

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
         * Generate a config file if one doesn't exist.
         * */
        if (!(file.exists() && !file.isDirectory()))
        {
            var cfg = new BotConfig();
            cfg.key = "insert Discord API key here";

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

    /**
     * Gets the Discord bot key
     * 
     * @return The key
     */
    public String getKey()
    {
        return key;
    }
}
