package cz.tefek.botdiril.userdata.card;

import java.util.ArrayList;
import java.util.List;

import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.util.BotdirilLog;

public class UserCards
{
    public static List<CardPair> getCards(DBConnection db, int fid)
    {
        return db.exec("SELECT * FROM " + UserInventory.TABLE_CARDS + " WHERE fk_us_id=? AND cr_amount>0", stat ->
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

                var cp = new CardPair(item, eq.getLong("cr_amount"));
                cp.setLevel(eq.getInt("cr_level"));
                cp.setLevel(eq.getInt("cr_xp"));

                cps.add(cp);
            }

            return cps;
        }, fid);
    }
}
