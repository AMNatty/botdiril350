package com.botdiril.userdata.tempstat;

public enum EnumBlessing
{
    UNBREAKABLE_PICKAXE(0, "Unbreakable Pickaxe", "Your pickaxe cannot break.", 60 * 30), //
    MEGAKEK_LOSS_IMMUNITY(1, "Blessed Megakeks", "The chance to lose megakeks is much lower.", 60 * 3), //
    CANT_BE_CURSED(2, "Curse Immunity", "You cannot be cursed while this blessing is active.", 60 * 30), //
    BETTER_SELL_PRICES(3, "Negotiation Skill", "You have much better sell values.", 60 * 5), //
    CHANCE_NOT_TO_CONSUME_KEY(4, "Skeleton Key", "Chance to not consume a key on use.", 60 * 12), //
    STEAL_IMMUNE(5, "Arcane Safe", "You cannot be robbed, under any circumstances.", 60 * 30), //
    MINE_SURGE(6, "Mining Surge", "Mining has a chance not to go on cooldown.", 60 * 12), //
    PICKPOCKET(7, "Pickpocket 100", "You have a chance to steal much more than you normally could.", 60 * 60 * 2), //
    CRAFTING_SURGE(8, "Crafting Surge", "Chance to not consume ingredients while crafting.", 60 * 4), //
    NUKE_IMMUNE(9, "Tactical anti-ballistic missile system", "You cannot be nuked.", 60 * 60); //

    public static final int MAX_BLESSINGS = 32;

    private final int id;
    private final String localizedName;
    private final String description;
    private final long durationInSeconds;

     EnumBlessing(int id, String localizedName, String description, long durationInSeconds)
    {
        this.id = id;
        this.localizedName = localizedName;
        this.description = description;
        this.durationInSeconds = durationInSeconds;
    }

    public String getDescription()
    {
        return this.description;
    }

    public long getDurationInSeconds()
    {
        return this.durationInSeconds;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }

    public int getID()
    {
        return this.id;
    }
}
