package cz.tefek.botdiril.userdata.item;

import cz.tefek.botdiril.userdata.EnumCurrency;

public class ItemCurrency extends Item
{
    private final EnumCurrency currency;

    public ItemCurrency(EnumCurrency currency)
    {
        super(currency.getName(), currency.getIcon(), currency.getLocalizedName(), currency.getDescription());

        this.currency = currency;
    }

    public EnumCurrency getCurrency()
    {
        return currency;
    }
}
