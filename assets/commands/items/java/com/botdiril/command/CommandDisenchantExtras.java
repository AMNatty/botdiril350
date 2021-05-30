package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.userdata.items.Items;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilLog;

import java.util.concurrent.atomic.AtomicLong;

@Command("disenchantextras")
public class CommandDisenchantExtras
{
    @CmdInvoke
    public static void dust(CommandContext co)
    {
        var cards = new AtomicLong();
        var dust = new AtomicLong();

        co.db.exec("SELECT * FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount > 1", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Card.getCardByID(ilID);

                if (item == null)
                {
                    BotdirilLog.logger.warn(String.format("User FID %d has a null item in their inventory! ID: %d", co.inventory.getFID(), ilID));
                    continue;
                }

                var amountToDust = eq.getLong("cr_amount") - 1;
                cards.addAndGet(amountToDust);
                dust.addAndGet(ShopEntries.getDustForDisenchanting(item) * amountToDust);
            }

            return true;
        }, co.inventory.getFID());

        co.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_CARDS + " SET cr_amount = 1 WHERE fk_us_id=? AND cr_amount > 1", co.inventory.getFID());

        co.inventory.addDust(dust.get());

        co.respond(String.format("*Disenchanted **%s %s cards** for **%s %s**.*",
            BotdirilFmt.format(cards.get()), Icons.CARDS,
            BotdirilFmt.format(dust.get()), Items.dust.getInlineDescription()));
    }
}
