package cz.tefek.botdiril.command.general;

import net.dv8tion.jda.api.EmbedBuilder;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;

@Command(value = "aliases", aliases = {
        "listaliases" }, category = CommandCategory.GENERAL, description = "List your aliases.")
public class CommandListAliases
{
    @CmdInvoke
    public static void list(CallObj co)
    {
        var bound = co.po.getUsedAliases();

        var eb = new EmbedBuilder();
        eb.setColor(0x008080);
        eb.setThumbnail(co.caller.getEffectiveAvatarUrl());
        eb.setAuthor(co.caller.getAsTag(), null, co.caller.getEffectiveAvatarUrl());

        if (bound == 0)
        {
            eb.setTitle("You have no aliases");
            eb.setDescription(String.format("Tip: Set up an alias using `%salias <what gets replaced> <what to replace it with>`.", co.usedPrefix));
        }
        else
        {
            eb.setTitle(String.format("You have %d aliases", Integer.bitCount(bound & 0x000000FF)));
            eb.setDescription("Currently set aliases:");
            for (int i = 0; i < Byte.SIZE; i++)
            {
                if ((1 << i & bound) > 0)
                {
                    var alias = co.po.getAlias(i);
                    eb.addField(String.format("Alias %d", i), String.format("`%s` → `%s`", alias.getKey(), alias.getValue()), false);
                }
            }

            eb.setFooter(String.format("Tip: Type `%sremovealias <alias number>` to delete an alias.", co.usedPrefix));
        }

        co.respond(eb.build());
    }
}
