package cz.tefek.botdiril.command.currency;

import java.util.concurrent.atomic.AtomicLong;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.icon.Icons;
import cz.tefek.botdiril.util.BotdirilLog;

@Command(value = "sellextras", aliases = { "sellduplicates", "selldupes",
        "sd" }, category = CommandCategory.ITEMS, description = "Sells your duplicate cards.")
public class CommandSellExtras
{
    @CmdInvoke
    public static void dust(CallObj co)
    {
        var cards = new AtomicLong();
        var coins = new AtomicLong();

        co.db.exec("SELECT * FROM " + UserInventory.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount > 1", stat ->
        {
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Card.getCardByID(ilID);

                if (item == null)
                {
                    BotdirilLog.logger.warn(String.format("User %d has a null item in their inventory! ID: %d", co.caller.getIdLong(), ilID));
                    continue;
                }

                var amountToSell = eq.getLong("cr_amount") - 1;
                cards.addAndGet(amountToSell);
                var cardLevel = eq.getInt("cr_level");
                coins.addAndGet(Card.getPrice(item, cardLevel) * amountToSell);
            }

            return true;
        }, co.ui.getFID());

        co.db.simpleUpdate("UPDATE " + UserInventory.TABLE_CARDS + " SET cr_amount = 1 WHERE fk_us_id=? AND cr_amount > 1", co.ui.getFID());

        CommandSell.sellRoutine(co, Icons.CARDS + " Cards", cards.get(), coins.get());
    }
}
