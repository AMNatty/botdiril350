package com.botdiril.command.superuser;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.InventoryTables;

@Command("resetcooldowns")
public class CommandResetCooldowns
{
    @CmdInvoke
    public static void resetCooldowns(CommandContext co)
    {
        resetCooldowns(co, co.player);
    }

    @CmdInvoke
    public static void resetCooldowns(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        co.db.exec("DELETE FROM " + InventoryTables.TABLE_TIMERS + " WHERE fk_us_id=?", stat ->
        {
            var res = stat.executeUpdate();

            if (res == 0)
            {
                co.respond("No timers were reset.");
                return 0;
            }

            co.respondf("Reset **%d** timer(s) of **%s**.", res, player.getMention());
            return res;

        }, player.inventory().getFID());
    }
}
