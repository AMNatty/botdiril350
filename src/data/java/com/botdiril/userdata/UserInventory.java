package com.botdiril.userdata;

import com.botdiril.framework.command.CommandStorage;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.sql.DBConnection;
import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.EnumCardModifier;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemCurrency;
import com.botdiril.userdata.item.ItemDrops;
import com.botdiril.userdata.properties.PropertyObject;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.userdata.xp.XPRewards;

import java.util.Objects;
import java.util.stream.Collectors;

public abstract class UserInventory
{
    protected transient final DBConnection db;

    protected int fkid;

    protected UserInventory(DBConnection connection, int fkID)
    {
        this.db = connection;
        this.fkid = fkID;
    }

    public int getFID()
    {
        return this.fkid;
    }

    public UIObject getUserDataObj()
    {
        return this.db.exec("SELECT * FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?", stat ->
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
        return this.db.exec("SELECT * FROM " + InventoryTables.TABLE_ACHIEVEMENTS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var rs = stat.executeQuery();
            if (!rs.next())
            {
                this.db.simpleUpdate("INSERT INTO " + InventoryTables.TABLE_ACHIEVEMENTS + " (fk_us_id, fk_il_id) VALUES (?, ?)", this.fkid, achievement.getID());
                return true;
            }
            return false;
        }, this.fkid, achievement.getID());
    }

    public boolean hasAchievement(Achievement achievement)
    {
        return this.db.exec("SELECT * FROM " + InventoryTables.TABLE_ACHIEVEMENTS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
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
        return this.db.getValueOr("SELECT tm_time FROM " + InventoryTables.TABLE_TIMERS + " WHERE fk_us_id=? AND fk_il_id=?",
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
        this.db.exec("SELECT tm_time FROM " + InventoryTables.TABLE_TIMERS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_TIMERS + " SET tm_time=? WHERE fk_us_id=? AND fk_il_id=?", timestamp, this.fkid, timer.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + InventoryTables.TABLE_TIMERS + " (fk_us_id, fk_il_id, tm_time)  VALUES (?, ?, ?)", this.fkid, timer.getID(), timestamp);
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
        return this.db.getValueOr("SELECT cr_amount FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?",
            "cr_amount", Long.class, 0L, this.fkid, card.getID());
    }

    public long howManyOf(Item item)
    {
        if (item instanceof ItemCurrency curr)
        {
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

        return this.db.getValueOr("SELECT it_amount FROM " + InventoryTables.TABLE_INVENTORY + " WHERE fk_us_id=? AND fk_il_id=?",
            "it_amount", Long.class, 0L, this.fkid, item.getID());
    }

    public long getCards()
    {
        return this.db.getValueOr("SELECT SUM(cr_amount) as cardcount FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=?",
            "cardcount", Long.class, 0L, this.fkid);
    }

    public int getCardLevel(Card card)
    {
        return this.db.getValueOr("SELECT cr_level FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?",
            "cr_level", Integer.class, 0, this.fkid, card.getID());
    }

    public long getCardXP(Card card)
    {
        return this.db.getValueOr("SELECT cr_xp FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?",
            "cr_xp", Long.class, 0L, this.fkid, card.getID());
    }

    public long getCoins()
    {
        return this.db.getValueOr("SELECT us_coins FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_coins", Long.class, 0L, this.fkid);
    }

    public long getDust()
    {
        return this.db.getValueOr("SELECT us_dust FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_dust", Long.class, 0L, this.fkid);
    }

    public long getKeks()
    {
        return this.db.getValueOr("SELECT us_keks FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_keks", Long.class, 0L, this.fkid);
    }

    public long getKekTokens()
    {
        return this.db.getValueOr("SELECT us_tokens FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_tokens", Long.class, 0L, this.fkid);
    }

    public long getKeys()
    {
        return this.db.getValueOr("SELECT us_keys FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_keys", Long.class, 0L, this.fkid);
    }

    public long getXP()
    {
        return this.db.getValueOr("SELECT us_xp FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_xp", Long.class, 0L, this.fkid);
    }

    public int getLevel()
    {
        return this.db.getValueOr("SELECT us_level FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_level", Integer.class, 0, this.fkid);
    }

    public long getMegaKeks()
    {
        return this.db.getValueOr("SELECT us_mega FROM " + InventoryTables.TABLE_USER + " WHERE us_id=?",
            "us_mega", Long.class, 0L, this.fkid);
    }

    ///
    /// ITEM SETS
    ///

    public void setCard(Card item, long amount)
    {
        this.db.exec("SELECT cr_amount FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_CARDS + " SET cr_amount=? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + InventoryTables.TABLE_CARDS + " (fk_us_id, fk_il_id, cr_amount)  VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void setItem(Item item, long amount)
    {
        if (item instanceof ItemCurrency curr)
        {
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

        this.db.exec("SELECT it_amount FROM " + InventoryTables.TABLE_INVENTORY + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_INVENTORY + " SET it_amount=? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + InventoryTables.TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount) VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void setCoins(long coins)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_coins=? WHERE us_id=?", coins, this.fkid);
    }

    public void setDust(long dust)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_dust=? WHERE us_id=?", dust, this.fkid);
    }

    public void setKeks(long keks)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_keks=? WHERE us_id=?", keks, this.fkid);
    }

    public void setKekTokens(long tokens)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_tokens=? WHERE us_id=?", tokens, this.fkid);
    }

    public void setKeys(long keys)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_keys=? WHERE us_id=?", keys, this.fkid);
    }

    public void setLevel(int level)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_level=? WHERE us_id=?", level, this.fkid);
    }

    public void setMegaKeks(long megas)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_mega=? WHERE us_id=?", megas, this.fkid);
    }

    public void setXP(long xp)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_xp=? WHERE us_id=?", xp, this.fkid);
    }

    ///
    /// ITEM ADDS
    ///

    private void addXP(long xp)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_xp=us_xp+? WHERE us_id=?", xp, this.fkid);
    }

    public void addXP(CommandContext co, long xp)
    {
        var preAddXP = this.getXP();
        var currentXP = preAddXP + xp;

        var lvl = this.getLevel();
        var newLevel = lvl;
        var xpSum = XPRewards.getXPAtLevel(newLevel);
        var consumedXP = 0L;

        var rewards = new ItemDrops();

        final var maxLevel = XPRewards.getMaxLevel();

        while (xpSum <= currentXP)
        {
            consumedXP = xpSum;

            if (++newLevel >= maxLevel)
            {
                this.setXP(0);
                consumedXP = currentXP;
                newLevel = maxLevel;
                break;
            }

            rewards.add(XPRewards.getRewardsForLvl(newLevel));

            xpSum += XPRewards.getXPAtLevel(newLevel);
        }

        this.setXP(currentXP - consumedXP);

        if (newLevel > lvl)
        {
            rewards.each(this::addItem);
            this.setLevel(newLevel);

            var rw = rewards.stream().map(ip -> ip.getAmount() + "x " + ip.getItem().inlineDescription()).collect(Collectors.joining("\n"));

            var cmds = CommandStorage.commandsInLevelRange(lvl, newLevel);

            if (cmds.size() > 0)
            {
                co.respondf("""
                ***You advanced to level %d!***
                **Rewards:**
                %s
                **You unlocked the following commands:**
                %s
                """, newLevel, rw, cmds.stream().map(cmd -> '`' + cmd.value() + '`').collect(Collectors.joining("\n")));
            }
            else
            {
                co.respondf("""
                ***You advanced to level %d!***
                **Rewards:**
                %s
                """, newLevel, rw);
            }
        }
    }

    public void addCard(Card item)
    {
        this.addCard(item, 1);
    }

    public void addCard(Card item, long amount)
    {
        this.db.exec("SELECT cr_amount FROM " + InventoryTables.TABLE_CARDS + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_CARDS + " SET cr_amount=cr_amount+? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                this.db.simpleUpdate("INSERT INTO " + InventoryTables.TABLE_CARDS + " (fk_us_id, fk_il_id, cr_amount) VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void addCardXP(CommandContext co, Card card, long xp)
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

        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_CARDS + " SET cr_xp=?, cr_level=? WHERE fk_us_id=? AND fk_il_id=?", newXP, newLevel, this.fkid, card.getID());

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
        if (item instanceof ItemCurrency curr)
        {
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

        this.db.exec("SELECT it_amount FROM " + InventoryTables.TABLE_INVENTORY + " WHERE fk_us_id=? AND fk_il_id=?", stat ->
        {
            var res = stat.executeQuery();

            if (res.next())
            {
                db.simpleUpdate("UPDATE " + InventoryTables.TABLE_INVENTORY + " SET it_amount=it_amount+? WHERE fk_us_id=? AND fk_il_id=?", amount, this.fkid, item.getID());
            }
            else
            {
                db.simpleUpdate("INSERT INTO " + InventoryTables.TABLE_INVENTORY + " (fk_us_id, fk_il_id, it_amount)  VALUES (?, ?, ?)", this.fkid, item.getID(), amount);
            }

            return null;
        }, this.fkid, item.getID());
    }

    public void addCoins(long coins)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_coins=us_coins+? WHERE us_id=?", coins, this.fkid);
    }

    public void addDust(long dust)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_dust=us_dust+? WHERE us_id=?", dust, this.fkid);
    }

    public void addKeks(long keks)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_keks=us_keks+? WHERE us_id=?", keks, this.fkid);
    }

    public void addKekTokens(long tokens)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_tokens=us_tokens+? WHERE us_id=?", tokens, this.fkid);
    }

    public void addKeys(long keys)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_keys=us_keys+? WHERE us_id=?", keys, this.fkid);
    }

    public void addLevel(int level)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_level=us_level+? WHERE us_id=?", level, this.fkid);
    }

    public void addMegaKeks(long megas)
    {
        this.db.simpleUpdate("UPDATE " + InventoryTables.TABLE_USER + " SET us_mega=us_mega+? WHERE us_id=?", megas, this.fkid);
    }

    public PropertyObject getPropertyObject()
    {
        return new PropertyObject(this.db, this.fkid);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInventory that = (UserInventory) o;
        return this.fkid == that.fkid;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.fkid);
    }
}
