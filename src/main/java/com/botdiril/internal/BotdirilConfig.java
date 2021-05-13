package com.botdiril.internal;

import com.botdiril.Botdiril;
import com.botdiril.MajorFailureException;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.botdiril.util.BotdirilLog;

public class BotdirilConfig
{
    @SerializedName("key")
    private String apiKey;

    @SerializedName("mysql_host")
    private String sqlHost;

    @SerializedName("mysql_user")
    private String sqlUser;

    @SerializedName("mysql_pass")
    private String sqlPass;

    @SerializedName("superusers_override")
    private long[] superusers;

    private transient List<Long> superuserList;

    private static final Path CONFIG_FILE = Path.of("settings.json");
    public static final String UNIVERSAL_PREFIX = "botdiril.";

    public static BotdirilConfig load() throws IOException
    {
        if (!Files.isRegularFile(CONFIG_FILE))
        {
            if (Files.exists(CONFIG_FILE))
            {
                throw new MajorFailureException("%s exists, but is not a regular file!".formatted(CONFIG_FILE));
            }

            var cfg = new BotdirilConfig();
            cfg.apiKey = "<insert Discord bot API key here>";
            cfg.sqlHost = "<insert MySQL hostname here>";
            cfg.sqlUser = "<insert MySQL username here>";
            cfg.sqlPass = "<insert MySQL password here>";
            cfg.superusers = getDefaultSuperusers();

            var gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();

            Files.writeString(CONFIG_FILE, gson.toJson(cfg));

            BotdirilLog.logger.error("Could not find %s, aborting.".formatted(CONFIG_FILE.toAbsolutePath()));
            BotdirilLog.logger.error("You need to set up the settings.json file I've just created.");
            BotdirilLog.logger.error("It's just some basic stuff like the API key.");

            throw new MajorFailureException("Unitialized config file!");
        }

        try (var reader = Files.newBufferedReader(CONFIG_FILE))
        {
            var gson = new Gson();
            var cfg = gson.fromJson(reader, BotdirilConfig.class);
            cfg.superuserList = Arrays.stream(Objects.requireNonNullElseGet(cfg.superusers, BotdirilConfig::getDefaultSuperusers))
                .boxed()
                .collect(Collectors.toUnmodifiableList());

            return cfg;
        }
    }

    private static long[] getDefaultSuperusers()
    {
        return new long[] { Botdiril.AUTHOR_ID };
    }

    public String getApiKey()
    {
        return this.apiKey;
    }

    public String getSqlHost()
    {
        return this.sqlHost;
    }

    public String getSqlUser()
    {
        return this.sqlUser;
    }

    public String getSqlPass()
    {
        return this.sqlPass;
    }

    public List<Long> getSuperuserOverrideIDs()
    {
        return this.superuserList;
    }
}
