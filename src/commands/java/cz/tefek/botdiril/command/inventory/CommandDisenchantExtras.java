package cz.tefek.botdiril.command.inventory;

import java.util.concurrent.atomic.AtomicLong;

import cz.tefek.botdiril.framework.command.CommandContext;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.userdata.items.Items;
import cz.tefek.botdiril.util.BotdirilFmt;
import cz.tefek.botdiril.util.BotdirilLog;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.userdata.item.ShopEntries;

@Command(value = "disenchantextras", aliases = { "disenchantduplicates", "disenchantdupes", "dustextras",
        "dustduplicates", "dustdupes",
        "dd" }, category = CommandCategory.ITEMS, description = "Disenchants your duplicate cards.")
public class CommandDisenchantExtras
{
    @CmdInvoke
    public static void dust(CommandContext co)
    {
        var cards = new AtomicLong();
        var dust = new AtomicLong();

        co.db.exec("SELECT * FROM " + UserInventory.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount > 1", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Card.getCardByID(ilID);

                if (item == null)
                {
                    BotdirilLog.logger.warn(String.format("User %d has a null item in their cz.tefek.botdiril.command.inventory! ID: %d", co.caller.getIdLong(), ilID));
                    continue;
                }

                var amountToDust = eq.getLong("cr_amount") - 1;
                cards.addAndGet(amountToDust);
                dust.addAndGet(ShopEntries.getDustForDisenchanting(item) * amountToDust);
            }

            return true;
        }, co.ui.getFID());

        co.db.simpleUpdate("UPDATE " + UserInventory.TABLE_CARDS + " SET cr_amount = 1 WHERE fk_us_id=? AND cr_amount > 1", co.ui.getFID());

        co.ui.addDust(dust.get());

        co.respond(String.format("*Disenchanted **%s %s cards** for **%s %s**.*",
            BotdirilFmt.format(cards.get()), Icons.CARDS,
            BotdirilFmt.format(dust.get()), Items.dust.inlineDescription()));
    }
}
