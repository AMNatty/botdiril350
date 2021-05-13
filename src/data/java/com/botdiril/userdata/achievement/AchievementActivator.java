package com.botdiril.userdata.achievement;

import net.dv8tion.jda.api.entities.User;

import com.botdiril.framework.command.CommandContext;
import com.botdiril.userdata.UserInventory;

public class AchievementActivator
{
    public static boolean fire(CommandContext co, UserInventory ui, User user, Achievement achievement)
    {
        if (!ui.hasAchievement(achievement))
        {
            ui.fireAchievement(achievement);

            if (co.ui.getFID() == ui.getFID())
                co.respond("You've obtained the **" + achievement.inlineDescription() + "** achievement - " + achievement.getDescription());
            else
                co.respond(user.getAsMention() + " has obtained the **" + achievement.inlineDescription() + "** achievement - " + achievement.getDescription());

            return true;
        }

        return false;
    }
}
