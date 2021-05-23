package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;

@Command("aliases")
public class CommandListAliases
{
    @CmdInvoke
    public static void list(ChatCommandContext co)
    {
        var bound = co.userProperties.getUsedAliases();

        var eb = new ResponseEmbed();
        eb.setColor(0x008080);
        eb.setThumbnail(co.player.getAvatarURL());
        eb.setAuthor(co.player.getTag(), null, co.player.getAvatarURL());

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
                    var alias = co.userProperties.getAlias(i);
                    eb.addField(String.format("Alias %d", i), String.format("`%s` → `%s`", alias.getKey(), alias.getValue()), false);
                }
            }

            eb.setFooter(String.format("Tip: Type `%sremovealias <alias number>` to delete an alias.", co.usedPrefix));
        }

        co.respond(eb);
    }
}
