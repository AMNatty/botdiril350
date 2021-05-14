package com.botdiril.command.superuser;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.achievement.AchievementActivator;

@Command(value = "triggerachievement", aliases = {
    "triggerac"
}, description = "Triggers an achievement for a user.", category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandTriggerAchievement
{
    @CmdInvoke
    public static void trigger(CommandContext co, @CmdPar("achievement") Achievement achievement)
    {
        if (!AchievementActivator.fire(co, co.player, achievement))
        {
            co.respond("*You already have this achievement!*");
        }
    }

    @CmdInvoke
    public static void trigger(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("achievement") Achievement achievement)
    {
        if (!AchievementActivator.fire(co, player, achievement))
        {
            co.respondf("***%s** already has this achievement!*", player.getMention());
        }
    }
}
