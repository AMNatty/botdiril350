package cz.tefek.botdiril.data.config.guild;

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
    private long id;
    private String prefix;
    private Long loggingChannel;

    public GuildConfig(long id)
    {
        this.id = id;

        try
        {
            this.deserialize();
        }
        catch (Exception e)
        {
            // It doesn't exist
        }
    }

    @Override
    public String getPath()
    {
        return "data/guild/g_" + id + ".json";
    }

    public String getPrefix()
    {
        return prefix;
    }

    public void setPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public Long getLoggingChannel()
    {
        return loggingChannel;
    }

    public void setLoggingChannel(long loggingChannel)
    {
        this.loggingChannel = loggingChannel;
    }
}
