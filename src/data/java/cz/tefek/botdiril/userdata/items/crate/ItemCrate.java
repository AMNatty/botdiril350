package cz.tefek.botdiril.userdata.items.crate;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.item.*;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.util.BotdirilFmt;

public abstract class ItemCrate extends Item implements IOpenable
{
    public static int DISPLAY_LIMIT = 20;

    public ItemCrate(String name, String icon, String localizedName, String description)
    {
        super(name, icon, localizedName, description);
    }

    @Override
    public String getFootnote(CallObj co)
    {
        return "Open using `" + co.usedPrefix + "open " + this.getName() + "`. Keep in mind you need a key to do so.";
    }

    @Override
    public boolean requiresKey()
    {
        return true;
    }

    protected abstract void addDrops(CallObj co, ItemDrops id);

    public int getDisplayLimit()
    {
        return DISPLAY_LIMIT;
    }

    @Override
    public void open(CallObj co, long amount)
    {
        var fm = String.format("**You open %d %s and get the following items:**", amount, this.getIcon());
        var sb = new StringBuilder(fm);

        var ip = new ItemDrops();

        for (int i = 0; i < amount; i++)
        {
            this.addDrops(co, ip);
        }

        var i = 0;

        var displayLimit = this.getDisplayLimit();

        for (var itemPair : ip)
        {
            var item = itemPair.getItem();
            var amt = itemPair.getAmount();

            co.ui.addItem(item, amt);

            if (i <= displayLimit)
            {
                sb.append(String.format("\n%sx %s", BotdirilFmt.format(amt), item.inlineDescription()));
            }

            i++;
        }

        var dc = ip.distintCount() - DISPLAY_LIMIT;

        if (dc > displayLimit)
        {
            sb.append(String.format("\nand %d more different items...", dc - displayLimit));
        }

        var itemCount = ip.stream()
            .filter(itemPair -> !(itemPair.getItem() instanceof ItemCurrency))
            .mapToLong(ItemPair::getAmount)
            .sum();
        sb.append(String.format("\n**Total %s items.**", BotdirilFmt.format(itemCount)));

        co.po.addStat(EnumStat.CRATES_OPENED, amount);

        co.respond(sb.toString());
    }
}
