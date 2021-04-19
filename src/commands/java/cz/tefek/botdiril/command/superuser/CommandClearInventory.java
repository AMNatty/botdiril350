package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.Member;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.userdata.UserInventory;

@Command(value = "clearinventory", aliases = {
        "clearinv" }, category = CommandCategory.SUPERUSER, description = "Wipe someone's inventory.", powerLevel = EnumPowerLevel.SUPERUSER_OVERRIDE)
public class CommandClearInventory
{
    @CmdInvoke
    public static void wipeSelf(CommandContext co)
    {
        wipe(co, co.callerMember);
    }

    @CmdInvoke
    public static void wipe(CommandContext co, @CmdPar("user") Member user)
    {
        co.db.exec("DELETE FROM " + UserInventory.TABLE_INVENTORY + " WHERE fk_us_id=?", stat ->
        {
            var res = stat.executeUpdate();

            if (res == 0)
            {
                co.respond("No items were deleted.");
                return 0;
            }

            co.respond(String.format("Deleted **%d** type(s) of items.", res));
            return res;

        }, new UserInventory(co.db, user.getUser().getIdLong()).getFID());
    }
}
