package com.botdiril.userdata.card;

public enum EnumCardModifier
{
    UNRANKED(0, "Unranked", 1, 1000),
    CARDBOARD(1, "Cardboard", 1.1, 2000),
    WOODEN(2, "Wooden", 1.2, 4000),
    BRONZE(3, "Bronze", 1.3, 8000),
    SILVER(4, "Silver", 1.4, 16000),
    GOLD(5, "Gold", 1.48, 32000),
    PLATINUM(6, "Platinum", 1.55, 64000),
    DIAMOND(7, "Diamond", 1.61, 128000),
    MASTER(8, "Master", 1.66, 256000),
    CHALLENGER(9, "Challenger", 1.7, 1024000),
    CHALLENGER_PLUS(10, "Challenger+", 1.7, 2048000),
    ASCENDED(11, "Ascended", 1.75, 4096000),
    ASCENDED_PLUS(12, "Ascended+", 1.75, Long.MAX_VALUE);

    private final int level;
    private final String localizedName;
    private final double skillMod;
    private final long xpForLevelUp;

    EnumCardModifier(int level, String localizedName, double skillMod, long xpForLevelUp)
    {
        this.level = level;
        this.localizedName = localizedName;
        this.skillMod = skillMod;
        this.xpForLevelUp = xpForLevelUp;
    }

    public int getLevel()
    {
        return this.level;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }

    public double getSkillMod()
    {
        return this.skillMod;
    }

    public long getXPForLevelUp()
    {
        return this.xpForLevelUp;
    }

    public static EnumCardModifier getByLevel(int level)
    {
        var vs = values();

        for (EnumCardModifier v : vs)
            if (v.level == level)
                return v;

        return null;
    }

    public static EnumCardModifier getMaxLevel()
    {
        return ASCENDED_PLUS;
    }
}
