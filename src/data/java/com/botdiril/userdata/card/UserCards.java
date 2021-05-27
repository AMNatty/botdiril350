package com.botdiril.userdata.card;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.util.BotdirilLog;

import java.util.ArrayList;
import java.util.List;

public class UserCards
{
    public static List<CardPair> getCards(DBConnection db, EntityPlayer player)
    {
        var inv = player.inventory();
        var fid = inv.getFID();

        return db.exec("SELECT * FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount>0", stat ->
        {
            var cps = new ArrayList<CardPair>();
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Card.getCardByID(ilID);

                if (item == null)
                {
                    BotdirilLog.logger.warn(String.format("User FID %d has a null card in their inventory! ID: %d", fid, ilID));
                    continue;
                }

                var cp = CardPair.of(item, eq.getLong("cr_amount"));
                cp.setLevel(eq.getInt("cr_level"));
                cp.setXP(eq.getInt("cr_xp"));

                cps.add(cp);
            }

            return cps;
        }, fid);
    }
}
