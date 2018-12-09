package cz.tefek.botdiril.data.config.guild;

import cz.tefek.botdiril.data.IDataJson;

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
}
