package cz.tefek.botdiril.serverdata;

import com.mysql.cj.MysqlType;

import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.framework.sql.ParamNull;

public class ServerConfig
{
    private final long serverID;

    private long loggingChannel;
    private String prefix;

    public ServerConfig(long serverID)
    {
        this.serverID = serverID;
    }

    public long getServerID()
    {
        return serverID;
    }

    public long getLoggingChannel()
    {
        return loggingChannel;
    }

    void loadLoggingChannel(long loggingChannel)
    {
        this.loggingChannel = loggingChannel;
    }

    public void setLoggingChannel(DBConnection db, long loggingChannel)
    {
        this.loggingChannel = loggingChannel;

        db.simpleUpdate("UPDATE " + ServerPreferences.PREF_TABLE + " SET sc_logchannel=? WHERE sc_guild=?", this.loggingChannel, this.serverID);
    }

    public void loadPrefix(String prefix)
    {
        this.prefix = prefix;
    }

    public void setPrefix(DBConnection db, String prefix)
    {
        this.prefix = prefix;

        db.simpleUpdate("UPDATE " + ServerPreferences.PREF_TABLE + " SET sc_prefix = ? WHERE sc_guild = ?", this.prefix == null ? new ParamNull(MysqlType.VARCHAR) : this.prefix, this.serverID);
    }

    public String getPrefix()
    {
        return prefix == null ? "botdiril." : prefix;
    }
}
