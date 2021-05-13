package com.botdiril.command.currency;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilFmt;
import net.dv8tion.jda.api.EmbedBuilder;

import java.text.MessageFormat;
import java.util.Locale;

import com.botdiril.userdata.UserInventory;

@Command(value = "xpleaderboards", aliases = { "toplevel", "toplevels",
        "xprankings" }, category = CommandCategory.CURRENCY, description = "Shows the top 10 highest level users.")
public class CommandTopLevel
{
    public static final int LIMIT = 10;

    @CmdInvoke
    public static void show(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setAuthor("Global level leaderboards");
        eb.setDescription(MessageFormat.format("Showing max {0} users.", LIMIT));
        eb.setColor(0x008080);
        eb.setThumbnail(co.jda.getSelfUser().getEffectiveAvatarUrl());

        co.db.setAutocommit(true);
        co.db.exec("SELECT us_userid, us_level, us_xp FROM " + UserInventory.TABLE_USER + " WHERE us_userid<>? ORDER BY us_level DESC, us_xp DESC LIMIT " + LIMIT, stat ->
        {
            var rs = stat.executeQuery();

            int i = 1;

            while (rs.next())
            {
                var level = rs.getInt("us_level");
                var xp = rs.getLong("us_xp");
                var usn = String.format(Locale.ROOT, "**%d.** <@%d>", i, rs.getLong("us_userid"));
                var lvlInfo = String.format("**Level %d**, %d XP", level, xp);
                var row = String.format("%s with %s,\nwhich is **%s XP** total.", usn, lvlInfo, BotdirilFmt.format(XPRewards.getCumulativeXPForLevel(level) + xp));

                eb.addField("", row, false);

                i++;
            }

            return 0;
        }, Botdiril.AUTHOR_ID);
        co.db.setAutocommit(false);

        co.respond(eb);
    }
}
