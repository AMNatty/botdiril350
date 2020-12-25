package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.achievement.Achievement;
import cz.tefek.botdiril.userdata.achievement.AchievementActivator;

@Command(value = "triggerachievement", aliases = {
    "triggerac"
}, description = "Triggers an achievement for a user.", category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandTriggerAchievement
{
    @CmdInvoke
    public static void trigger(CallObj co, @CmdPar("achievement") Achievement achievement)
    {
        if (!AchievementActivator.fire(co, co.ui, co.caller, achievement))
        {
            co.respond("*You already have this achievement!*");
        }
    }

    @CmdInvoke
    public static void trigger(CallObj co, @CmdPar("user") User user, @CmdPar("achievement") Achievement achievement)
    {
        if (!AchievementActivator.fire(co, new UserInventory(co.db, user.getIdLong()), user, achievement))
        {
            co.respond("*%s already has this achievement!*".formatted(user.getAsMention()));
        }
    }
}
