package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.framework.permission.PowerLevel;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.serverdata.RolePreferences;
import net.dv8tion.jda.api.entities.Role;

import java.text.MessageFormat;

@Command(value = "bind", aliases = "bindpower", category = CommandCategory.ADMINISTRATIVE, description = "Bind a power to a role.", powerLevel = EnumPowerLevel.SUPERUSER)
public class CommandBindPower
{
    @CmdInvoke
    public static void bind(CommandContext co, @CmdPar("role") Role role, @CmdPar("power") EnumPowerLevel powerLevel)
    {
        var mp = PowerLevel.getManageablePowers(co.db, co.callerMember, co.textChannel);

        CommandAssert.assertTrue(mp.contains(powerLevel), "You can't manage that power or it is not assignable to a role.");
        CommandAssert.assertTrue(co.callerMember.canInteract(role), "You can't manage that role!");

        var res = RolePreferences.add(co.db, role, powerLevel);

        switch (res)
        {
            case RolePreferences.ADDED -> co.respond(MessageFormat.format("Added **{0}** to **{1}**.", powerLevel.toString(), role.getName()));
            case RolePreferences.ALREADY_EXISTS -> co.respond(MessageFormat.format("**{0}** is already bound to **{1}**.", powerLevel.toString(), role.getName()));
            default -> throw new CommandException(MessageFormat.format("Unexpected behaviour detected in `{0}`, please report this to a developer. Response code: `{1}`", CommandBindPower.class.getCanonicalName(), res));
        }
    }
}
