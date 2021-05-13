package com.botdiril.userdata.card;

public class CardSet
{
    public static CardSet league;
    public static CardSet terraria;
    public static CardSet csgo;

    private final String setName;
    private final String setLocalizedName;
    private final String setPrefix;

    private final boolean drops;

    private final String description;

    public CardSet(String collectionName, String collectionLocalizedName, String collectionPrefix, boolean drops, String description)
    {
        this.setName = collectionName;
        this.setLocalizedName = collectionLocalizedName;
        this.setPrefix = collectionPrefix;
        this.drops = drops;
        this.description = description;
    }

    public boolean canDrop()
    {
        return this.drops;
    }

    public String getDescription()
    {
        return this.description;
    }

    public String getSetLocalizedName()
    {
        return this.setLocalizedName;
    }

    public String getSetName()
    {
        return this.setName;
    }

    public String getSetPrefix()
    {
        return this.setPrefix;
    }
}
