package com.botdiril.framework.sql;

import com.botdiril.util.BotdirilLog;
import com.mchange.v2.c3p0.impl.NewProxyPreparedStatement;
import com.mysql.cj.jdbc.ClientPreparedStatement;
import org.intellij.lang.annotations.Language;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Optional;

public record DBConnection(Connection connection) implements AutoCloseable
{
    public void setAutoCommit(boolean autocommit)
    {
        try
        {
            this.connection.setAutoCommit(autocommit);
        }
        catch (SQLException e)
        {
            throw new DBException(e);
        }
    }

    private void setParams(PreparedStatement statement, Object... params) throws Exception
    {
        for (int i = 0; i < params.length; i++)
        {
            var param = params[i];

            if (param == null)
            {
                throw new IllegalStateException("Parameter can't be raw null!");
            }

            int paramIdx = i + 1;

            if (param instanceof ParamNull paramNull)
            {
                statement.setNull(paramIdx, paramNull.type().getJdbcType());
            }
            else if (param instanceof Integer intVal)
            {
                statement.setInt(paramIdx, intVal);
            }
            else if (param instanceof Long longVal)
            {
                statement.setLong(paramIdx, longVal);
            }
            else if (param instanceof String str)
            {
                statement.setString(paramIdx, str);
            }
            else if (param instanceof byte[] bytes)
            {
                ByteArrayInputStream stream = new ByteArrayInputStream(bytes);
                statement.setBinaryStream(paramIdx, stream);
            }
            else
            {
                throw new UnsupportedOperationException("Unsupported DB data type.");
            }
        }
    }

    public int simpleUpdate(@Language("MySQL") String statement, Object... params)
    {
        try
        {
            try (var stat = connection.prepareStatement(statement))
            {
                this.setParams(stat, params);

                BotdirilLog.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                return stat.executeUpdate();
            }
        }
        catch (Exception e)
        {
            throw new DBException(e);
        }
    }

    public boolean simpleExecute(@Language("MySQL") String statement, Object... params)
    {
        try
        {
            try (var stat = connection.prepareStatement(statement))
            {
                this.setParams(stat, params);

                BotdirilLog.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                return stat.execute();
            }
        }
        catch (Exception e)
        {
            throw new DBException(e);
        }
    }

    public <R> R exec(@Language("MySQL") String statement, SqlStatementCallback<PreparedStatement, R> callback, Object... params)
    {
        try
        {
            try (var stat = connection.prepareStatement(statement))
            {
                this.setParams(stat, params);

                BotdirilLog.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                return callback.exec(stat);
            }
        }
        catch (Exception e)
        {
            throw new DBException(e);
        }
    }

    public <R> R exec(@Language("MySQL") String statement, @MagicConstant(intValues = {
        Statement.RETURN_GENERATED_KEYS,
        Statement.NO_GENERATED_KEYS
    }) int generateKeys, SqlStatementCallback<PreparedStatement, R> callback, Object... params)
    {
        try
        {
            try (var stat = connection.prepareStatement(statement, generateKeys))
            {
                this.setParams(stat, params);

                BotdirilLog.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                return callback.exec(stat);
            }
        }
        catch (Exception e)
        {
            throw new DBException(e);
        }
    }

    private Optional<byte[]> retrieveBlob(ResultSet resultSet, String columnName) throws SQLException, IOException
    {
        var blob = resultSet.getBlob(columnName);

        if (resultSet.wasNull())
        {

            blob.free();
            return Optional.empty();
        }

        try (var is = blob.getBinaryStream())
        {
            var bytes = new byte[(int) blob.length()];

            if (is.read(bytes) != bytes.length)
                throw new DBException("Blob read size mismatch.");

            return Optional.of(bytes);
        }
        finally
        {
            blob.free();
        }
    }

    private <R> Optional<R> retrieveValue(ResultSet resultSet, String columnName, Class<R> valueType) throws SQLException, IOException
    {
        if (valueType == byte[].class)
        {
            return this.retrieveBlob(resultSet, columnName).map(valueType::cast);
        }

        Object val;

        if (valueType == Integer.class)
        {
            val = resultSet.getInt(columnName);
        }
        else if (valueType == Long.class)
        {
            val = resultSet.getLong(columnName);
        }
        else if (valueType == Float.class)
        {
            val = resultSet.getFloat(columnName);
        }
        else if (valueType == Double.class)
        {
            val = resultSet.getDouble(columnName);
        }
        else if (valueType == String.class)
        {
            val = resultSet.getString(columnName);
        }
        else
        {
            throw new UnsupportedOperationException(String.format("Unsupported type %s.", valueType.getName()));
        }

        return resultSet.wasNull() ? Optional.empty() : Optional.of(valueType.cast(val));
    }

    public <R> Optional<R> getValue(@Language("MySQL") String statement, String columnName, Class<R> valueType, Object... params)
    {
        try
        {
            try (var stat = connection.prepareStatement(statement))
            {
                this.setParams(stat, params);

                BotdirilLog.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                try (var rs = stat.executeQuery())
                {
                    if (!rs.next())
                        return Optional.empty();

                    return this.retrieveValue(rs, columnName, valueType);
                }
            }
        }
        catch (Exception e)
        {
            throw new DBException(e);
        }
    }

    public @NotNull <R> R getValueOr(@Language("MySQL") String statement, String columnName, Class<R> valueType, R fallbackValue, Object... params)
    {
        try
        {
            try (var stat = connection.prepareStatement(statement))
            {
                this.setParams(stat, params);

                BotdirilLog.logger.debug("Executing SQL: " + ((ClientPreparedStatement) ((NewProxyPreparedStatement) stat).unwrap(ClientPreparedStatement.class)).asSql());

                try (var rs = stat.executeQuery())
                {
                    if (!rs.next())
                        return fallbackValue;

                    return this.retrieveValue(rs, columnName, valueType).orElse(fallbackValue);
                }
            }
        }
        catch (Exception e)
        {
            throw new DBException(e);
        }
    }

    @Override
    public void close() throws Exception
    {
        this.connection.close();
    }

    public void commit()
    {
        try
        {
            this.connection.commit();
        }
        catch (SQLException e)
        {
            throw new DBException(e);
        }
    }

    public void rollback()
    {
        try
        {
            this.connection.rollback();
        }
        catch (SQLException e)
        {
            throw new DBException(e);
        }
    }
}
