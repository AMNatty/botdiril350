package cz.tefek.botdiril.framework.sql;

import com.mysql.cj.MysqlType;

public class ParamNull
{
    private final MysqlType type;

    public ParamNull(MysqlType type)
    {
        this.type = type;
    }

    public MysqlType getType()
    {
        return type;
    }
}
