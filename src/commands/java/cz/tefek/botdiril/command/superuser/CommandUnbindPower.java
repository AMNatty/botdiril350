package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.Role;

import java.text.MessageFormat;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.permission.PowerLevel;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.serverdata.RolePreferences;

@Command(value = "unbind", aliases = "unbindpower", category = CommandCategory.ADMINISTRATIVE, description = "Unbind a power from a role.", powerLevel = EnumPowerLevel.SUPERUSER)
public class CommandUnbindPower
{
    @CmdInvoke
    public static void bind(CommandContext co, @CmdPar("role") Role role, @CmdPar("power") EnumPowerLevel powerLevel)
    {
        var mp = PowerLevel.getManageablePowers(co.db, co.callerMember, co.textChannel);

        CommandAssert.assertTrue(mp.contains(powerLevel), "You can't manage that power.");
        CommandAssert.assertTrue(co.callerMember.canInteract(role), "You can't manage that role!");

        var res = RolePreferences.add(co.db, role, powerLevel);

        var response = switch (res) {
            case RolePreferences.REMOVED -> MessageFormat.format("Removed **{0}** from **{1}**.", powerLevel.toString(), role.getName());
            case RolePreferences.NOT_PRESENT -> MessageFormat.format("**{0}** is not bound to **{1}**...", powerLevel.toString(), role.getName());
            default -> throw new CommandException(MessageFormat.format("Unexpected behaviour detected in the cz.tefek.botdiril.command.command, please report this to a developer.. Response code: {0}", res));
        };

        co.respond(response);
    }
}
