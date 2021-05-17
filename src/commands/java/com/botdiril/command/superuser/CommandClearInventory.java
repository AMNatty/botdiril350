package com.botdiril.command.superuser;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.userdata.InventoryTables;

@Command("clearinventory")
public class CommandClearInventory
{
    @CmdInvoke
    public static void wipeSelf(CommandContext co)
    {
        wipe(co, co.player);
    }

    @CmdInvoke
    public static void wipe(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        co.db.exec("DELETE FROM " + InventoryTables.TABLE_INVENTORY + " WHERE fk_us_id=?", stat ->
        {
            var res = stat.executeUpdate();

            if (res == 0)
            {
                co.respond("No items were deleted.");
                return 0;
            }

            co.respond(String.format("Deleted **%d** type(s) of items.", res));
            return res;

        }, player.inventory().getFID());
    }
}
