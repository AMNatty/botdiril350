package com.botdiril.framework;

import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.UserInventory;

import java.util.Objects;

public abstract class EntityPlayer
{
    protected final DBConnection db;

    protected String tag;
    protected String mention;
    protected String name;
    protected String avatarURL;

    protected UserInventory cachedInventory;

    protected EntityPlayer(DBConnection db)
    {
        this.db = db;
    }

    public String getMention()
    {
        return this.mention;
    }

    public String getTag()
    {
        return this.tag;
    }

    public String getName()
    {
        return this.name;
    }

    public String getAvatarURL()
    {
        return this.avatarURL;
    }

    protected abstract UserInventory loadInventory();

    public UserInventory inventory()
    {
        if (this.cachedInventory == null)
            this.cachedInventory = this.loadInventory();

        return this.cachedInventory;
    }

    @Override
    public final int hashCode()
    {
        return Objects.hashCode(this.inventory().getFID());
    }

    /**
     * Overriding classes MUST make sure they do not change the semantics of this equals.
     *
     * The only reason this method is not final is to allow optimizations, such as avoiding loading the entire
     * inventory just to check equality.
     * */
    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof EntityPlayer entityPlayer)
        {
            return this.inventory().equals(entityPlayer.inventory());
        }

        return false;
    }
}
