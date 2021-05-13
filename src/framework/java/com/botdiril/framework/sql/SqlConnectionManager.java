package com.botdiril.framework.sql;

import com.botdiril.BotMain;
import com.mchange.v2.c3p0.ComboPooledDataSource;

import java.beans.PropertyVetoException;
import java.sql.SQLException;

public class SqlConnectionManager
{
    private static final int IDLE_CONNECTION_TEST_PERIOD = 60;
    private static final int MAX_CONNECTION_AGE = 60 * 60;

    private final ComboPooledDataSource dataSource;

    public SqlConnectionManager() throws PropertyVetoException
    {
        this.dataSource = new ComboPooledDataSource();
        this.dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
        this.dataSource.setIdleConnectionTestPeriod(IDLE_CONNECTION_TEST_PERIOD);
        this.dataSource.setTestConnectionOnCheckin(true);
        this.dataSource.setMaxConnectionAge(MAX_CONNECTION_AGE);
        this.dataSource.setJdbcUrl("jdbc:mysql:// " + BotMain.config.getSqlHost() + "/" + SqlFoundation.SCHEMA + "?useUnicode=true&autoReconnect=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
        this.dataSource.setUser(BotMain.config.getSqlUser());
        this.dataSource.setPassword(BotMain.config.getSqlPass());
        this.dataSource.setAutoCommitOnClose(false);
    }

    public DBConnection getConnection()
    {
        return getConnection(false);
    }

    public DBConnection getConnection(boolean autocommit)
    {
        try
        {
            var c = this.dataSource.getConnection();
            c.setAutoCommit(autocommit);
            return new DBConnection(c);
        }
        catch (SQLException e)
        {
            throw new DBException(e);
        }
    }
}
