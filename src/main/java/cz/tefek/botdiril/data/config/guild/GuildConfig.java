package cz.tefek.botdiril.data.config.guild;

import cz.tefek.botdiril.BotMain;
import cz.tefek.botdiril.data.IDataJson;

/**
 * Object class for instances of guild configurations. These will be pretty much
 * always loaded in memory to dramatically lower disk reads for roughly 30 bytes
 * of memory per guild.
 * 
 * <p>
 * <i>Note that setters do not automatically serialize on value change.</i>
 * </p>
 */
public class GuildConfig implements IDataJson
{
    /**
     * ID of the guild.
     */
    private long id;

    /**
     * Bot's prefix for that guild.
     */
    private String prefix;

    /**
     * The logging channel in that guild.
     */
    private Long loggingChannel;

    /**
     * The constructor for {@link GuildConfig}. It takes the <code>id</code> as the
     * single argument and attempts to construct the guild configuration from the
     * config file specified by {@link IDataJson#getPath()}, which may or may not
     * exist.
     */
    public GuildConfig(long id)
    {
        this.id = id;

        if (!this.configFileAvailable())
        {
            BotMain.logger.info(String.format("Creating config for guild %d.", this.id));

            this.prefix = null;
            this.loggingChannel = null;
            return;
        }

        try
        {
            this.deserialize();
        }
        catch (Exception e)
        {
            BotMain.logger.info(String.format("Couldn't load guild config for %d, will use the defaults.", this.id));

            this.prefix = null;
            this.loggingChannel = null;
        }
    }

    public Long getLoggingChannel()
    {
        return this.loggingChannel;
    }

    @Override
    public String getPath()
    {
        return "data/guild/g_" + this.id + ".json";
    }

    public String getPrefix()
    {
        return this.prefix;
    }

    public void setLoggingChannel(long loggingChannel)
    {
        this.loggingChannel = loggingChannel;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }
}
