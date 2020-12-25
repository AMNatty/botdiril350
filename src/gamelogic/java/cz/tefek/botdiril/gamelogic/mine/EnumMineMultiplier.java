package cz.tefek.botdiril.gamelogic.mine;

public enum EnumMineMultiplier
{
    MLT_RANDOM("random"),
    MLT_EXPERIENCE("level"),
    MLT_KITLESS("no repair kit");

    private final String localizedName;

    EnumMineMultiplier(String localizedName)
    {
        this.localizedName = localizedName;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }
}
