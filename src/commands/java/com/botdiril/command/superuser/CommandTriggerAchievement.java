package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.achievement.AchievementActivator;
import net.dv8tion.jda.api.entities.User;

import com.botdiril.userdata.UserInventory;

@Command(value = "triggerachievement", aliases = {
    "triggerac"
}, description = "Triggers an achievement for a user.", category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandTriggerAchievement
{
    @CmdInvoke
    public static void trigger(CommandContext co, @CmdPar("achievement") Achievement achievement)
    {
        if (!AchievementActivator.fire(co, co.ui, co.caller, achievement))
        {
            co.respond("*You already have this achievement!*");
        }
    }

    @CmdInvoke
    public static void trigger(CommandContext co, @CmdPar("user") User user, @CmdPar("achievement") Achievement achievement)
    {
        if (!AchievementActivator.fire(co, new UserInventory(co.db, user.getIdLong()), user, achievement))
        {
            co.respond("*%s already has this achievement!*".formatted(user.getAsMention()));
        }
    }
}
