package com.botdiril.userdata.items.pickaxe;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PickaxeMetadata
{
    @JsonProperty("roman_numeral")
    private String romanNumeral;

    @JsonProperty("tier")
    private int tier;

    @JsonProperty("mini_tier")
    private int miniTier;

    @JsonProperty("budget")
    private long budget;

    @JsonProperty("break_chance")
    private double breakChance;

    @JsonProperty("multiplier")
    private double multiplier;

    public double getBreakChance()
    {
        return this.breakChance;
    }

    public long getBudget()
    {
        return this.budget;
    }

    public int getMiniTier()
    {
        return this.miniTier;
    }

    public String getRomanNumeral()
    {
        return this.romanNumeral;
    }

    public int getTier()
    {
        return this.tier;
    }

    public double getMultiplier()
    {
        return this.multiplier;
    }
}
