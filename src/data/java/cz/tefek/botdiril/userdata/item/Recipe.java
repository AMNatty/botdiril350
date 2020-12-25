package cz.tefek.botdiril.userdata.item;

import java.util.List;

import cz.tefek.botdiril.userdata.IIdentifiable;

public class Recipe
{
    private final List<ItemPair> components;
    private final long yields;
    private final IIdentifiable result;

    public Recipe(List<ItemPair> components, long yields, IIdentifiable result)
    {
        this.components = components;
        this.yields = yields;
        this.result = result;
    }

    public long getAmount()
    {
        return this.yields;
    }

    public List<ItemPair> getComponents()
    {
        return this.components;
    }

    public IIdentifiable getResult()
    {
        return this.result;
    }
}
