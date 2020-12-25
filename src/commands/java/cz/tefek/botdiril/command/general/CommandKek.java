package cz.tefek.botdiril.command.general;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.EnumSpecialCommandProperty;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.util.BotdirilRnd;

@Command(category = CommandCategory.GENERAL, description = Icons.KEK, value = "kek", special = {
        EnumSpecialCommandProperty.ALLOW_LOCK_BYPASS })
public class CommandKek
{
    @CmdInvoke
    public static void name(CallObj co)
    {
        co.respond(BotdirilRnd.choose(Icons.SKINS));
    }
}
