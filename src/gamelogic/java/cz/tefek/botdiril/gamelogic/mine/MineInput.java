package cz.tefek.botdiril.gamelogic.mine;

import cz.tefek.botdiril.userdata.items.pickaxe.ItemPickaxe;

public class MineInput
{
    private final ItemPickaxe pickaxe;

    private final long repairKitsAvailable;
    private final long userLevel;

    private boolean cursedDoubleBreak;

    private boolean blessedUnbreakablePickaxe;
    private boolean blessedMiningSurge;

    private boolean preferenceRepairKitEnabled;

    public MineInput(ItemPickaxe pickaxe, long repairKitsAvailable, long userLevel)
    {
        this.pickaxe = pickaxe;
        this.repairKitsAvailable = repairKitsAvailable;
        this.userLevel = userLevel;
    }

    public boolean isCursedDoubleBreak()
    {
        return this.cursedDoubleBreak;
    }

    public void setCursedDoubleBreak(boolean cursedDoubleBreak)
    {
        this.cursedDoubleBreak = cursedDoubleBreak;
    }

    public boolean isPickaxeBreakable()
    {
        return !this.blessedUnbreakablePickaxe;
    }

    public void setBlessedUnbreakablePickaxe(boolean blessedUnbreakablePickaxe)
    {
        this.blessedUnbreakablePickaxe = blessedUnbreakablePickaxe;
    }

    public boolean isBlessedMiningSurge()
    {
        return this.blessedMiningSurge;
    }

    public void setBlessedMiningSurge(boolean blessedMiningSurge)
    {
        this.blessedMiningSurge = blessedMiningSurge;
    }

    public boolean isPreferenceRepairKitEnabled()
    {
        return this.preferenceRepairKitEnabled;
    }

    public void setPreferenceRepairKitEnabled(boolean preferenceRepairKitEnabled)
    {
        this.preferenceRepairKitEnabled = preferenceRepairKitEnabled;
    }

    public ItemPickaxe getPickaxe()
    {
        return this.pickaxe;
    }

    public long getRepairKitsAvailable()
    {
        return this.repairKitsAvailable;
    }

    public long getUserLevel()
    {
        return this.userLevel;
    }
}
