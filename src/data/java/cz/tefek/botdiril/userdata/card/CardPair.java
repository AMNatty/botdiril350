package cz.tefek.botdiril.userdata.card;

public class CardPair
{
    private final Card card;
    private long amount;
    private int level;
    private long xp;

    private CardPair(Card item)
    {
        this.card = item;
        this.amount = 1;
    }

    private CardPair(Card item, long amount)
    {
        this.card = item;
        this.amount = amount;
    }

    public static CardPair of(Card item)
    {
        return new CardPair(item);
    }

    public static CardPair of(Card item, long amount)
    {
        return new CardPair(item, amount);
    }

    public void addAmount(long amt)
    {
        this.amount += amt;
    }

    public long getAmount()
    {
        return this.amount;
    }

    public Card getCard()
    {
        return this.card;
    }

    public int getLevel()
    {
        return this.level;
    }

    public long getXP()
    {
        return this.xp;
    }

    public void increment()
    {
        this.amount++;
    }

    public void setLevel(int level)
    {
        this.level = level;
    }

    public void setXP(long xp)
    {
        this.xp = xp;
    }
}
