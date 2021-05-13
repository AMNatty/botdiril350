package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.EnumSpecialCommandProperty;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import net.dv8tion.jda.api.entities.Emote;

@Command(value = "react", description = "Makes the bot react with an emote.",
    category = CommandCategory.GENERAL, special = EnumSpecialCommandProperty.ALLOW_LOCK_BYPASS)
public class CommandReact
{
    @CmdInvoke
    public static void react(CommandContext co, @CmdPar("emote") Emote emote)
    {
        co.message.addReaction(emote).submit();
    }
}
