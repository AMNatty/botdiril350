package com.botdiril.command.currency;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.util.BotdirilFmt;

import java.text.MessageFormat;
import java.util.Locale;

@Command("richkeks")
public class CommandTopKeks
{
    public static final int LIMIT = 10;

    @CmdInvoke
    public static void show(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setAuthor("Richest kek owners");
        eb.setDescription(MessageFormat.format("Showing max {0} users.", LIMIT));
        eb.setColor(0x008080);
        eb.setThumbnail(co.botIconURL);

        co.db.setAutoCommit(true);
        co.db.exec("SELECT us_userid, us_keks FROM " + InventoryTables.TABLE_USER + " WHERE us_userid<>? ORDER BY us_keks DESC LIMIT " + LIMIT, stat ->
        {
            var rs = stat.executeQuery();

            int i = 1;

            while (rs.next())
            {
                var usn = String.format(Locale.ROOT, "**%d.** <@%d>", i, rs.getLong("us_userid"));
                var row = String.format("%s with %s", usn, BotdirilFmt.amountOfMD(rs.getLong("us_keks"), Icons.KEK));

                eb.addField("", row, false);

                i++;
            }

            return 0;
        }, Botdiril.AUTHOR_ID);
        co.db.setAutoCommit(false);

        co.respond(eb);
    }
}
