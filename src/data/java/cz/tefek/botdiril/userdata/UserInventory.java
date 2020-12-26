package cz.tefek.botdiril.userdata;

import java.sql.Statement;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.CommandStorage;
import cz.tefek.botdiril.framework.sql.DBConnection;
import cz.tefek.botdiril.framework.sql.SqlFoundation;
import cz.tefek.botdiril.userdata.achievement.Achievement;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.card.EnumCardModifier;
import cz.tefek.botdiril.userdata.item.Item;
import cz.tefek.botdiril.userdata.item.ItemCurrency;
import cz.tefek.botdiril.userdata.item.ItemDrops;
import cz.tefek.botdiril.userdata.timers.EnumTimer;
import cz.tefek.botdiril.userdata.timers.TimerUtil;
import cz.tefek.botdiril.userdata.xp.XPRewards;

public class UserInventory
{
    public static final String TABLE_USER = "users";
    public static final String TABLE_INVENTORY = "inventory";
    public static final String TABLE_CARDS = "cards";
    public static final String TABLE_ACHIEVEMENTS = "achievements";
    public static final String TABLE_GIFTCODES = "giftcodes";
    public static final String TABLE_TIMERS = "timers";

    public static void initTables(DBConnection db)
    {
        var tabExists = SqlFoundation.checkTableExists(db, TABLE_USER);

        if (!tabExists)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_USER + "( " +
                             "us_id INT PRIMARY KEY AUTO_INCREMENT, " +
                             "us_userid BIGINT NOT NULL UNIQUE, " +
                             "us_coins BIGINT NOT NULL DEFAULT 0, " +
                             "us_keks BIGINT NOT NULL DEFAULT 0, " +
                             "us_tokens BIGINT NOT NULL DEFAULT 0, " +
                             "us_keys BIGINT NOT NULL DEFAULT 0, " +
                             "us_mega BIGINT NOT NULL DEFAULT 0, " +
                             "us_dust BIGINT NOT NULL DEFAULT 0, " +
                             "us_level INT NOT NULL DEFAULT '1', " +
                             "us_xp BIGINT NOT NULL DEFAULT 0);");
        }

        var tabExistsInv = SqlFoundation.checkTableExists(db, TABLE_INVENTORY);

        if (!tabExistsInv)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_INVENTORY + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "it_amount BIGINT NOT NULL DEFAULT 0, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id) );");
        }

        var tabExistsCard = SqlFoundation.checkTableExists(db, TABLE_CARDS);

        if (!tabExistsCard)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_CARDS + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "cr_amount BIGINT NOT NULL DEFAULT 0, " +
                             "cr_level INT NOT NULL DEFAULT 0, " +
                             "cr_xp BIGINT NOT NULL DEFAULT 0, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id));");
        }

        var tabExistsAchv = SqlFoundation.checkTableExists(db, TABLE_ACHIEVEMENTS);

        if (!tabExistsAchv)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_ACHIEVEMENTS + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id));");
        }

        var tabExistsCodes = SqlFoundation.checkTableExists(db, TABLE_GIFTCODES);

        if (!tabExistsCodes)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_GIFTCODES + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "cd_code VARCHAR(32) NOT NULL, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id));");
        }

        var tabExistsTime = SqlFoundation.checkTableExists(db, TABLE_TIMERS);

        if (!tabExistsTime)
        {
            db.simpleExecute("CREATE TABLE " + TABLE_TIMERS + "(" +
                             "fk_us_id INT NOT NULL, " +
                             "fk_il_id INT NOT NULL, " +
                             "tm_time BIGINT NOT NULL, " +
                             "FOREIGN KEY (fk_us_id) REFERENCES " + TABLE_USER + "(us_id), " +
                             "FOREIGN KEY (fk_il_id) REFERENCES " + ItemLookup.TABLE_ITEMLOOKUP + "(il_id));");
        }
    }

    private final DBConnection db;

    private final int fkid;

    private final long userid;

    public UserInventory(DBConnection connection, long userid)
    {
        this.db = connection;
        this.userid = userid;

        var user = db.getValue("SELECT us_id FROM " + TABLE_USER + " WHERE us_userid=?", "us_id", Integer.class, this.userid);

        if (user.isEmpty())
        {
            this.fkid = db.exec("INSERT INTO " + TABLE_USER + "(us_userid) VALUES (?)", Statement.RETURN_GENERATED_KEYS, stat -> {
                stat.setLong(1, this.userid);
                stat.executeUpdate();
                var keys = stat.getGeneratedKeys();
                keys.next();
                return keys.getInt(1);
            });
        }
        else
        {
            this.fkid = user.get();
        }
    }

    public int getFID()
    {
        return this.fkid;
    }

    public UIObject getUserDataObj()
    {
        return this.db.exec("SELECT * FROM " + TABLE_USER + " WHERE us_id=?", stat ->
        {
            var rs = stat.executeQuery();
            rs.next();

            return new UIObject(rs.getInt("us_level"),
                rs.getLong("us_xp"),
                rs.getLong("us_coins"),
                rs.getLong("us_keks"),
                rs.getLong("us_dust"),
                rs.getLong("us_mega"),
                rs.getLong("us_keys"),
                rs.getLong("us_tokens"),
                this.getCards());
        }, this.fkid);
    }

    ///
    /// ACHIEVEMENTS
    ///

    public boolean fireAchievement(Achievement achievement)
    {
        return this.db.exec("SELECT * FROM " + TABLE_ACHIEVEMENTS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var rs = stat.executeQuery();
            if (!rs.next())
            {
                this.db.simpleUpdate("INSERT INTO " + TABLE_ACHIEVEMENTS + " (fk_us_id, fk_il_id) VALUES (?, ?)", this.fkid, achievement.getID());
                return true;
            }
            return false;
        }, this.fkid, achievement.getID());
    }

    public boolean hasAchievement(Achievement achievement)
    {
        return this.db.exec("SELECT * FROM " + TABLE_ACHIEVEMENTS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var rs = stat.executeQuery();

            return rs.next();
        }, this.fkid, achievement.getID());
    }

    ///
    /// TIMERS
    ///

    public long getTimer(EnumTimer timer)
    {
        return this.db.getValueOr("SELECT tm_time FROM " + TABLE_TIMERS + " WHERE fk_us_id=? AND fk_il_id=?",
            "tm_time", Long.class, TimerUtil.TIMER_OFF_COOLDOWN, this.fkid, timer.getID());
    }

    public long checkTimer(EnumTimer timer)
    {
        var tt = this.getTimer(timer);
        long currentTime = System.currentTimeMillis();

        if (currentTime > tt)
        {
            return TimerUtil.TIMER_OFF_COOLDOWN;
        }

        return tt - currentTime;
    }

    public void resetTimer(EnumTimer timer)
    {
        this.setTimer(timer, 0);
    }

    public void setTimer(EnumTimer timer, long timestamp)
    {
        this.db.exec("SELECT tm_time FROM " + TABLE_TIMERS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + TABLE_TIMERS + " SET tm_time=? WHERE fk_us_id=? AND fk_il_id=?", timestamp, this.fkid, timer.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + TABLE_TIMERS + " (fk_us_id, fk_il_id, tm_time)  VALUES (?, ?, ?)", this.fkid, timer.getID(), timestamp);
            }

            return null;
        }, this.fkid, timer.getID());
    }

    public long useTimer(EnumTimer timer)
    {
        var tt = this.getTimer(timer);
        long currentTime = System.currentTimeMillis();

        if (currentTime > tt)
        {
            this.setTimer(timer, currentTime + timer.getTimeOffset());
            return TimerUtil.TIMER_OFF_COOLDOWN;
        }
        return tt - currentTime;
    }

    // This differentiates in the fact that this overrides the time even when
    // waiting
    public long useTimerOverride(EnumTimer timer)
    {
        var tt = this.getTimer(timer);
        long currentTime = System.currentTimeMillis();

        if (currentTime > tt)
        {
            this.setTimer(timer, currentTime + timer.getTimeOffset());
            return TimerUtil.TIMER_OFF_COOLDOWN;
        }

        this.setTimer(timer, currentTime + timer.getTimeOffset());
        return tt - currentTime;
    }

    ///
    /// ITEM GETS
    ///

    public long howManyOf(Card card)
    {
        return this.db.getValueOr("SELECT cr_amount FROM " + TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?",
            "cr_amount", Long.class, 0L, this.fkid, card.getID());
    }

    public long howManyOf(Item item)
    {
        if (item instanceof ItemCurrency)
        {
            ItemCurrency curr = (ItemCurrency) item;

            return switch (curr.getCurrency())
            {
                case COINS -> this.getCoins();
                case DUST -> this.getDust();
                case KEKS -> this.getKeks();
                case KEYS -> this.getKeys();
                case MEGAKEKS -> this.getMegaKeks();
                case TOKENS -> this.getKekTokens();
                case XP -> this.getXP();
            };
        }

        return this.db.getValueOr("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=? AND fk_il_id=?",
            "it_amount", Long.class, 0L, this.fkid, item.getID());
    }

    public long getCards()
    {
        return this.db.getValueOr("SELECT SUM(cr_amount) as cardcount FROM " + TABLE_CARDS + " WHERE fk_us_id=?",
            "cardcount", Long.class, 0L, this.fkid);
    }

    public int getCardLevel(Card card)
    {
        return this.db.getValueOr("SELECT cr_level FROM " + TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?",
            "cr_level", Integer.class, 0, this.fkid, card.getID());
    }

    public long getCardXP(Card card)
    {
        return this.db.getValueOr("SELECT cr_xp FROM " + TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?",
            "cr_xp", Long.class, 0L, this.fkid, card.getID());
    }

    public long getCoins()
    {
        return this.db.getValueOr("SELECT us_coins FROM " + TABLE_USER + " WHERE us_id=?",
            "us_coins", Long.class, 0L, this.fkid);
    }

    public long getDust()
    {
        return this.db.getValueOr("SELECT us_dust FROM " + TABLE_USER + " WHERE us_id=?",
            "us_dust", Long.class, 0L, this.fkid);
    }

    public long getKeks()
    {
        return this.db.getValueOr("SELECT us_keks FROM " + TABLE_USER + " WHERE us_id=?",
            "us_keks", Long.class, 0L, this.fkid);
    }

    public long getKekTokens()
    {
        return this.db.getValueOr("SELECT us_tokens FROM " + TABLE_USER + " WHERE us_id=?",
            "us_tokens", Long.class, 0L, this.fkid);
    }

    public long getKeys()
    {
        return this.db.getValueOr("SELECT us_keys FROM " + TABLE_USER + " WHERE us_id=?",
            "us_keys", Long.class, 0L, this.fkid);
    }

    public long getXP()
    {
        return this.db.getValueOr("SELECT us_xp FROM " + TABLE_USER + " WHERE us_id=?",
            "us_xp", Long.class, 0L, this.fkid);
    }

    public int getLevel()
    {
        return this.db.getValueOr("SELECT us_level FROM " + TABLE_USER + " WHERE us_id=?",
            "us_level", Integer.class, 0, this.fkid);
    }

    public long getMegaKeks()
    {
        return this.db.getValueOr("SELECT us_mega FROM " + TABLE_USER + " WHERE us_id=?",
            "us_mega", Long.class, 0L, this.fkid);
    }

    ///
    /// ITEM SETS
    ///

    public void setCard(Card item, long amount)
    {
        this.db.exec("SELECT cr_amount FROM " + TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + TABLE_CARDS + " SET cr_amount=? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + TABLE_CARDS + " (fk_us_id, fk_il_id, cr_amount)  VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void setItem(Item item, long amount)
    {
        if (item instanceof ItemCurrency)
        {
            ItemCurrency curr = (ItemCurrency) item;

            switch (curr.getCurrency())
            {
                case COINS -> this.setCoins(amount);
                case DUST -> this.setDust(amount);
                case KEKS -> this.setKeks(amount);
                case KEYS -> this.setKeys(amount);
                case MEGAKEKS -> this.setMegaKeks(amount);
                case TOKENS -> this.setKekTokens(amount);
                case XP -> this.setXP(amount);
            }

            return;
        }

        this.db.exec("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + TABLE_INVENTORY + " SET it_amount=? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount) VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void setCoins(long coins)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_coins=? WHERE us_id=?", coins, this.fkid);
    }

    public void setDust(long dust)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_dust=? WHERE us_id=?", dust, this.fkid);
    }

    public void setKeks(long keks)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_keks=? WHERE us_id=?", keks, this.fkid);
    }

    public void setKekTokens(long tokens)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_tokens=? WHERE us_id=?", tokens, this.fkid);
    }

    public void setKeys(long keys)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_keys=? WHERE us_id=?", keys, this.fkid);
    }

    public void setLevel(int level)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_level=? WHERE us_id=?", level, this.fkid);
    }

    public void setMegaKeks(long megas)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_mega=? WHERE us_id=?", megas, this.fkid);
    }

    public void setXP(long xp)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_xp=? WHERE us_id=?", xp, this.fkid);
    }

    ///
    /// ITEM ADDS
    ///

    private void addXP(long xp)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_xp=us_xp+? WHERE us_id=?", xp, this.fkid);
    }

    public void addXP(CallObj co, long xp)
    {
        var preAddXP = this.getXP();
        var currentXP = preAddXP + xp;

        var lvl = this.getLevel();
        var i = lvl;
        var xpSum = XPRewards.getXPAtLevel(i);

        var rewards = new ItemDrops();

        final var maxLevel = XPRewards.getMaxLevel();

        while (xpSum <= currentXP)
        {
            if (++i >= maxLevel)
            {
                this.setXP(0);
                i = maxLevel;
                break;
            }

            rewards.add(XPRewards.getRewardsForLvl(i));

            xpSum += XPRewards.getXPAtLevel(i);
        }

        if (i > lvl)
        {
            this.setXP(currentXP - (xpSum - XPRewards.getXPAtLevel(i)));
            rewards.each(this::addItem);
            this.setLevel(i);

            var rw = rewards.stream().map(ip -> ip.getAmount() + "x " + ip.getItem().inlineDescription()).collect(Collectors.joining("\n"));

            var cmds = CommandStorage.commandsInLevelRange(lvl, i);

            if (cmds.size() > 0)
            {
                co.respond(String.format("***You advanced to level %d!***\n**Rewards:**\n%s\n**You unlocked the following commands:**\n%s", i, rw, cmds.stream().map(cmd -> '`' + cmd.value() + '`').collect(Collectors.joining("\n"))));
            }
            else
            {
                co.respond(String.format("***You advanced to level %d!***\n**Rewards:**\n%s", i, rw));
            }
        }
    }

    public void addCard(Card item)
    {
        this.addCard(item, 1);
    }

    public void addCard(Card item, long amount)
    {
        this.db.exec("SELECT cr_amount FROM " + TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + TABLE_CARDS + " SET cr_amount=cr_amount+? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + TABLE_CARDS + " (fk_us_id, fk_il_id, cr_amount) VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void addCardXP(CallObj co, Card card, long xp)
    {
        var preAddXP = this.getCardXP(card);
        var currentXP = preAddXP + xp;
        var lvl = this.getCardLevel(card);

        var maxTier = EnumCardModifier.getMaxLevel();
        var maxLevel = maxTier.getLevel();

        var newLevel = lvl;

        var tier = EnumCardModifier.getByLevel(newLevel);
        assert tier != null;
        var xpSum = tier.getXPForLevelUp();
        var consumedXP = 0L;

        while (xpSum <= currentXP)
        {
            consumedXP = xpSum;

            if (++newLevel >= maxLevel)
            {
                newLevel = maxLevel;
                break;
            }

            tier = EnumCardModifier.getByLevel(newLevel);
            assert tier != null;
            xpSum += tier.getXPForLevelUp();
        }

        var newXP = currentXP - consumedXP;

        this.db.simpleUpdate("UPDATE " + TABLE_CARDS + " SET cr_xp=?, cr_level=? WHERE fk_us_id=? AND fk_il_id=?", newXP, newLevel, this.fkid, card.getID());

        if (newLevel > lvl)
        {
            co.respond(String.format("*Your **%s** advanced to **level %d**: **%s**!*", card.inlineDescription(), newLevel, tier.getLocalizedName()));
        }
    }

    public void addItem(Item item)
    {
        this.addItem(item, 1);
    }

    public void addItem(Item item, long amount)
    {
        if (item instanceof ItemCurrency)
        {
            ItemCurrency curr = (ItemCurrency) item;

            switch (curr.getCurrency())
            {
                case COINS -> this.addCoins(amount);
                case DUST -> this.addDust(amount);
                case KEKS -> this.addKeks(amount);
                case KEYS -> this.addKeys(amount);
                case MEGAKEKS -> this.addMegaKeks(amount);
                case TOKENS -> this.addKekTokens(amount);
                case XP -> this.addXP(amount);
            }

            return;
        }

        this.db.exec("SELECT it_amount FROM " + TABLE_INVENTORY + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                db.simpleUpdate("UPDATE " + TABLE_INVENTORY + " SET it_amount=it_amount+? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                db.simpleUpdate("INSERT INTO " + TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount)  VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void addCoins(long coins)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_coins=us_coins+? WHERE us_id=?", coins, this.fkid);
    }

    public void addDust(long dust)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_dust=us_dust+? WHERE us_id=?", dust, this.fkid);
    }

    public void addKeks(long keks)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_keks=us_keks+? WHERE us_id=?", keks, this.fkid);
    }

    public void addKekTokens(long tokens)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_tokens=us_tokens+? WHERE us_id=?", tokens, this.fkid);
    }

    public void addKeys(long keys)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_keys=us_keys+? WHERE us_id=?", keys, this.fkid);
    }

    public void addLevel(int level)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_level=us_level+? WHERE us_id=?", level, this.fkid);
    }

    public void addMegaKeks(long megas)
    {
        this.db.simpleUpdate("UPDATE " + TABLE_USER + " SET us_mega=us_mega+? WHERE us_id=?", megas, this.fkid);
    }
}
