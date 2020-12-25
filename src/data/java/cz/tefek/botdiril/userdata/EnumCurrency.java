package cz.tefek.botdiril.userdata;

import cz.tefek.botdiril.userdata.icon.Icons;

public enum EnumCurrency
{
    XP("xp", Icons.ACHIEVEMENT_BOT, "Experience", "The main cz.tefek.botdiril.command.currency of Botdiril! Collect experience to unlock more items, loot and commands."),
    COINS("coin", Icons.COIN, "Coin", "Used mostly to buy goods and exchange for other currencies."),
    KEKS("kek", Icons.KEK, "Kek", "The main cz.tefek.botdiril.command.gambling cz.tefek.botdiril.command.currency, can be exchanged for Kek Tokens."),
    TOKENS("kektoken", Icons.TOKEN, "Kek Token", "Allow you to get some sweet loot for your cz.tefek.botdiril.command.gambling activities."),
    MEGAKEKS("megakek", Icons.MEGAKEK, "MegaKek", "Keks but on steroids. Reach one googol MegaKeks in no time!"),
    DUST("dust", Icons.DUST, "Dust", "Used mostly for crafting, Dust allows you to recycle duplicate items for new goods."),
    KEYS("key", Icons.KEY, "Key", "Unlock even more loot paths.");

    private final String localizedName;
    private final String desc;
    private final String name;
    private final String icon;

    EnumCurrency(String name, String icon, String localizedName, String description)
    {
        this.localizedName = localizedName;
        this.icon = icon;
        this.desc = description;
        this.name = name;
    }

    public String getLocalizedName()
    {
        return localizedName;
    }

    public String getDescription()
    {
        return desc;
    }

    public String getName()
    {
        return name;
    }

    public String getIcon()
    {
        return icon;
    }
}
