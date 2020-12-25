package cz.tefek.botdiril.userdata.items.pickaxe;

import cz.tefek.botdiril.userdata.item.Item;

public class ItemPickaxe extends Item
{
    private final double multiplier;
    private final long pickaxeWorth;
    private final double chanceToBreak;
    private final ItemPickaxe previousPickaxe;

    public ItemPickaxe(String name, String icon, String localizedName, double multiplier, long pickaxeWorth, double chanceToBreak, ItemPickaxe prevPickaxe, String description)
    {
        super(name, icon, localizedName, description);
        this.multiplier = multiplier;
        this.pickaxeWorth = pickaxeWorth;
        this.chanceToBreak = chanceToBreak;
        this.previousPickaxe = prevPickaxe;
    }

    public double getChanceToBreak()
    {
        return this.chanceToBreak;
    }

    public double getRareDropMultiplier()
    {
        return this.multiplier;
    }

    public long getPickaxeValue()
    {
        return this.pickaxeWorth;
    }

    public ItemPickaxe getPreviousPickaxe()
    {
        return this.previousPickaxe;
    }
}
