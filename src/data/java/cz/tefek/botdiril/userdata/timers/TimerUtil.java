package cz.tefek.botdiril.userdata.timers;

import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.pluto.chrono.MiniTime;

public class TimerUtil
{
    public static final long TIMER_OFF_COOLDOWN = -1;

    /**
     * Put a $ somewhere in the message to print the time there
     */
    public static void require(UserInventory ui, EnumTimer timer, String errorMessage)
    {
        var tm = ui.useTimer(timer);

        if (tm != TIMER_OFF_COOLDOWN)
        {
            throw new CommandException(errorMessage.replaceAll("\\$", MiniTime.formatDiff(tm)));
        }
    }

    public static boolean tryConsume(UserInventory ui, EnumTimer timer)
    {
        return ui.useTimer(timer) == TIMER_OFF_COOLDOWN;
    }
}
