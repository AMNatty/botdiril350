package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.EnumSpecialCommandProperty;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.util.BotdirilRnd;

@Command(category = CommandCategory.GENERAL, description = Icons.KEK, value = "kek", special = {
        EnumSpecialCommandProperty.ALLOW_LOCK_BYPASS })
public class CommandKek
{
    @CmdInvoke
    public static void name(ChatCommandContext co)
    {
        co.respond(BotdirilRnd.choose(Icons.SKINS));
    }
}
