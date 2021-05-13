package com.botdiril.userdata;

public class UIObject
{
    private final int level;
    private final long xp;
    private final long coins;
    private final long keks;
    private final long dust;
    private final long megakeks;
    private final long keys;
    private final long tokens;
    private final long cards;

    public UIObject(int level, long xp, long coins, long keks, long dust, long megakeks, long keys, long tokens, long cards)
    {
        this.level = level;
        this.xp = xp;
        this.coins = coins;
        this.keks = keks;
        this.dust = dust;
        this.megakeks = megakeks;
        this.keys = keys;
        this.tokens = tokens;
        this.cards = cards;
    }

    public int getLevel()
    {
        return this.level;
    }

    public long getXP()
    {
        return this.xp;
    }

    public long getCoins()
    {
        return this.coins;
    }

    public long getKeks()
    {
        return this.keks;
    }

    public long getDust()
    {
        return this.dust;
    }

    public long getMegaKeks()
    {
        return this.megakeks;
    }

    public long getKeys()
    {
        return this.keys;
    }

    public long getTokens()
    {
        return this.tokens;
    }

    public long getCards()
    {
        return this.cards;
    }
}
