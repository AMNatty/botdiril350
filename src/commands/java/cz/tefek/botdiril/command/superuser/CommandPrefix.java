package cz.tefek.botdiril.command.superuser;


import cz.tefek.botdiril.Botdiril;
import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;

@Command(value = "prefix", category = CommandCategory.SUPERUSER, description = "Sets the prefix for this server.", powerLevel = EnumPowerLevel.SUPERUSER)
public class CommandPrefix
{
    @CmdInvoke
    public static void setPrefix(CommandContext co, @CmdPar("prefix") String prefix)
    {
        CommandAssert.stringNotTooLong(prefix, 8, "The prefix is too long.");

        if (prefix.contains("@"))
        {
            throw new CommandException("The prefix can't contain @.");
        }

        co.sc.setPrefix(co.db, prefix);

        co.guild.retrieveMember(co.bot).queue(member -> member.modifyNickname("[%s] %s".formatted(prefix, Botdiril.BRANDING)).complete());
        co.respond("Prefix set to: `%s`".formatted(prefix));
    }
}
