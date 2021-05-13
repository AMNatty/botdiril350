package com.botdiril.userdata.tempstat;

public enum EnumCurse
{
    CURSE_OF_YASUO(0, "Curse of Yasuo", "All cards drawn are now Yasuo.", 60 * 30), //
    DOUBLE_CHANCE_TO_LOSE_MEGA(1, "Cursed Megakeks", "Double chance to lose everything.", 60 * 60), //
    DOUBLE_PICKAXE_BREAK_CHANCE(2, "Mining Fatigue", "Double chance to break your pickaxe while mining.", 60 * 45), //
    CANT_TAKE_DAILY(3, "Daily Lockout", "You can't take your daily loot.", 60 * 120), //
    HALVED_SELL_VALUE(4, "Bad Negotiation", "You are really bad at selling goods.", 60 * 35), //
    CRAFTING_MAY_FAIL(5, "Crafting Fatigue", "Crafting may fail, consuming all used items.", 60 * 25), //
    CANT_SEE_MINED_STUFF(6, "Blindness", "You can't see what you mined.", 60 * 35), //
    CANT_WIN_JACKPOT(7, "Rigged", "You can't win the jackpot.", 60 * 120), //
    MAGNETIC(8, "Magnetic", "There is a chance your gift ends up redirected to me.", 60 * 60), // Your gifts may end up redirected to the bot
    EASIER_TO_ROB(9, "Loose Pockets", "You are an easier target to rob.", 60 * 30); //

    public static final int MAX_CURSES = 32;

    private final int id;
    private final String localizedName;
    private final String description;
    private final long durationInSeconds;

    EnumCurse(int id, String localizedName, String description, long durationInSeconds)
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
