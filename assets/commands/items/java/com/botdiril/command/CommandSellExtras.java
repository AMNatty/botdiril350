package com.botdiril.command;

import com.botdiril.command.CommandSell;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.util.BotdirilLog;

import java.util.concurrent.atomic.AtomicLong;

@Command("sellextras")
public class CommandSellExtras
{
    @CmdInvoke
    public static void sellDuplicates(CommandContext co)
    {
        var cards = new AtomicLong();
        var coins = new AtomicLong();

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

                var amountToSell = eq.getLong("cr_amount") - 1;
                cards.addAndGet(amountToSell);
                var cardLevel = eq.getInt("cr_level");
                coins.addAndGet(Card.getPrice(item, cardLevel) * amountToSell);
            }

            return true;
        }, co.inventory.getFID());

        co.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_CARDS + " SET cr_amount = 1 WHERE fk_us_id=? AND cr_amount > 1", co.inventory.getFID());

        CommandSell.sellRoutine(co, Icons.CARDS + " Cards", cards.get(), coins.get());
    }
}
