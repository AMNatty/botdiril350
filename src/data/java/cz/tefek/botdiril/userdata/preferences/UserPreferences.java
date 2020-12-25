package cz.tefek.botdiril.userdata.preferences;

import cz.tefek.botdiril.userdata.properties.PropertyObject;

public class UserPreferences
{
    public static final String PREFERENCE_BITFIELD = "preferences_bool_bitfield";

    public static boolean isBitEnabled(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();
        return (po.getPreferencesBitfield() & bit) != 0;
    }

    public static boolean toggleBit(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();
        var newVal = po.getPreferencesBitfield() ^ bit;

        po.setPreferencesBitfield(newVal);

        return (newVal & bit) != 0; // The new, toggled value
    }

    public static void setBit(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();

        po.setPreferencesBitfield(po.getPreferencesBitfield() | bit);
    }

    public static void clearBit(PropertyObject po, EnumUserPreference preference)
    {
        long bit = preference.getBit();

        po.setPreferencesBitfield(po.getPreferencesBitfield() & ~bit);
    }
}
