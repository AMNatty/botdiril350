package com.botdiril.userdata.stat;

public enum EnumStat
{
    COMMANDS_USED(0, "Commands used"),
    CRATES_OPENED(1, "Crates opened"),
    CARD_PACKS_OPENED(2, "Card packs opened"),
    TIMES_ROBBED(3, "Times you were robbed"),
    TIMES_NUKED(4, "Times you were nuked"),
    TIMES_MINED(5, "Times mined"),
    TIMES_FARMED(6, "Times farmed"),
    TIMES_DAILY(7, "Times daily used"),
    TIMES_LOST_ALL_MEGAKEKS(8, "Times megakeks lost"),
    ITEMS_CRAFTED(9, "Items crafted"),
    GIFTS_SENT(10, "Gifts sent"),
    BIGGEST_PAYOUT(11, "Most keks paid out"),
    BIGGEST_STEAL(12, "Most coins stolen"),
    BIGGEST_NUKE(13, "Most keks nuked away"),
    PICKAXES_BROKEN(14, "Pickaxes broken"),
    REPAIR_KITS_USED(15, "Pickaxe repair kits used");

    public static final int MAX_STATS = 48;

    private final int id;
    private final String localizedName;

    EnumStat(int id, String localizedName)
    {
        this.id = id;
        this.localizedName = localizedName;
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
