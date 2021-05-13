package com.botdiril.userdata.item;

public class ItemPair
{
    private final Item item;
    private long amount;

    private ItemPair(Item item, long amount)
    {
        this.item = item;
        this.amount = amount;
    }

    private ItemPair(Item item)
    {
        this.item = item;
        this.amount = 1;
    }

    public static ItemPair of(Item item, long amount)
    {
        return new ItemPair(item, amount);
    }

    public static ItemPair of(Item item)
    {
        return new ItemPair(item);
    }

    public Item getItem()
    {
        return item;
    }

    public long getAmount()
    {
        return amount;
    }

    public void addAmount(long amt)
    {
        this.amount += amt;
    }

    public void increment()
    {
        this.amount++;
    }
}
