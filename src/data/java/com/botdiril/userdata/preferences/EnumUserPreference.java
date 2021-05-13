package com.botdiril.userdata.preferences;

public enum EnumUserPreference
{
    REPAIR_KIT_DISABLED(1, "Disable repair kits"),
    GOLDEN_OIL_DISABLED(1 << 1, "Disable golden oil"),
    TOOLBOX_STEALING_DISABLED(1 << 2, "Disable toolbox stealing");

    private final long bit;
    private final String localizedName;

    EnumUserPreference(long bit, String localizedName)
    {
        this.bit = bit;
        this.localizedName = localizedName;
    }

    public long getBit()
    {
        return this.bit;
    }

    public String getLocalizedName()
    {
        return this.localizedName;
    }
}
