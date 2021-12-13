package com.botdiril.userdata.achievement;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.context.CommandContext;

public class AchievementActivator
{
    public static boolean fire(CommandContext co, EntityPlayer player, Achievement achievement)
    {
        var ui = player.inventory();

        if (!ui.hasAchievement(achievement))
        {
            ui.fireAchievement(achievement);

            if (co.player.equals(player))
                co.respondf("*You've obtained the **%s** achievement - %s.*", achievement, achievement.getDescription());
            else
                co.respondf("*%s has obtained the **%s** achievement - %s.*", player.getMention(), achievement, achievement.getDescription());

            return true;
        }

        return false;
    }
}
