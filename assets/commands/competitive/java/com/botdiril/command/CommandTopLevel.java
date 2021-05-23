package com.botdiril.command;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilFmt;

import java.text.MessageFormat;
import java.util.Locale;

@Command("xpleaderboards")
public class CommandTopLevel
{
    public static final int LIMIT = 10;

    @CmdInvoke
    public static void show(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setAuthor("Global level leaderboards");
        eb.setDescription(MessageFormat.format("Showing max {0} users.", LIMIT));
        eb.setColor(0x008080);
        eb.setThumbnail(co.botIconURL);

        co.db.setAutoCommit(true);
        co.db.exec("SELECT us_userid, us_level, us_xp FROM " + InventoryTables.TABLE_USER + " WHERE us_userid<>? ORDER BY us_level DESC, us_xp DESC LIMIT " + LIMIT, stat ->
        {
            var rs = stat.executeQuery();

            int i = 1;

            while (rs.next())
            {
                var level = rs.getInt("us_level");
                var xp = rs.getLong("us_xp");
                var usn = String.format(Locale.ROOT, "**%d.** <@%d>", i, rs.getLong("us_userid"));
                var lvlInfo = String.format("**Level %d** %s", level, BotdirilFmt.amountOf(xp, Icons.XP));
                var row = String.format("%s with %s,\nwhich is %s total.", usn, lvlInfo, BotdirilFmt.amountOfMD(XPRewards.getCumulativeXPForLevel(level) + xp, Icons.XP));

                eb.addField("", row, false);

                i++;
            }

            return 0;
        }, Botdiril.AUTHOR_ID);
        co.db.setAutoCommit(false);

        co.respond(eb);
    }
}
