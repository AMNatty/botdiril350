package com.botdiril.framework.sql;

import java.sql.SQLException;

@FunctionalInterface
public interface SqlStatementCallback<T, R>
{
    R exec(T c) throws SQLException;
}
