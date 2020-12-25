package cz.tefek.botdiril.serverdata;

import net.dv8tion.jda.api.entities.Role;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.util.BotdirilLog;

public class RolePreferences
{
    static final String TABLE_ROLEPOWERS = "rolepowers";

    public static final int ADDED = 1;
    public static final int ALREADY_EXISTS = 2;

    public static final int REMOVED = 1;
    public static final int NOT_PRESENT = 2;

    public static Set<EnumPowerLevel> getAllPowerLevels(DBConnection db, final List<Role> roles)
    {
        final var powerLevels = EnumSet.noneOf(EnumPowerLevel.class);

        if (roles.isEmpty())
        {
            return powerLevels;
        }

        var rolesIDsStringed = roles.stream().mapToLong(Role::getIdLong).mapToObj(l -> "rp_role=" + l).collect(Collectors.joining(" OR "));

        db.exec("SELECT DISTINCT rp_power FROM " + TABLE_ROLEPOWERS + " WHERE " + rolesIDsStringed, stat -> {
            var rs = stat.executeQuery();

            while (rs.next())
            {
                var id = rs.getString("rp_power");

                var powerLevel = EnumPowerLevel.getByID(id);

                if (powerLevel == null)
                    BotdirilLog.logger.error("Error: Could not find a power level with id '{}'.", id);

                powerLevels.add(powerLevel);
            }

            return true;
        });

        return powerLevels;
    }

    public static void load(DBConnection db)
    {
        var tabExists = SqlFoundation.checkTableExists(db, TABLE_ROLEPOWERS);

        if (!tabExists)
        {
            var values = Arrays.stream(EnumPowerLevel.values()).map(value -> "'" + value.getID() + "'").collect(Collectors.joining(", "));

            db.simpleExecute("CREATE TABLE " + TABLE_ROLEPOWERS + " ( " +
                             "rp_role BIGINT NOT NULL," +
                             " rp_power ENUM(" + values  + ") NOT NULL );");
        }
    }

    public static int add(DBConnection db, Role role, EnumPowerLevel powerLevel)
    {
        final var pID = powerLevel.getID();
        final var roleID = role.getIdLong();

        return db.exec("SELECT * FROM " + TABLE_ROLEPOWERS + " WHERE rp_role=(?) AND rp_power=(?)", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                return ALREADY_EXISTS;
            }
            else
            {
                db.simpleUpdate("INSERT INTO " + TABLE_ROLEPOWERS + " (rp_role, rp_power) VALUES (?, ?)", roleID, pID);

                return ADDED;
            }
        }, roleID, pID);
    }

    public static int unbind(DBConnection db, Role role, EnumPowerLevel powerLevel)
    {
        final var pID = powerLevel.getID();
        final var roleID = role.getIdLong();

        return db.exec("DELETE FROM " + TABLE_ROLEPOWERS + " WHERE rp_role=? AND rp_power=?", stmt ->
            stmt.executeUpdate() > 0 ? REMOVED : NOT_PRESENT, roleID, pID);
    }
}
