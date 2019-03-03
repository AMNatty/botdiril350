package cz.tefek.botdiril.data.config.guild;

import java.util.TreeMap;

public class GuildConfigMap extends TreeMap<Long, GuildConfig>
{
    /**
     * 
     */
    private static final long serialVersionUID = 8624204310268503779L;

    @Override
    public GuildConfig put(Long key, GuildConfig value)
    {
        if (key == null)
        {
            throw new IllegalArgumentException("Null is not a valid guild!");
        }

        if (value == null)
        {
            throw new IllegalArgumentException("Null is not a valid GuildConfig!");
        }

        return super.put(key, value);
    }
}