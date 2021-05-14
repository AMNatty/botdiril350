package com.botdiril.userdata.items.crate;

import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.item.*;
import com.botdiril.userdata.stat.EnumStat;
import com.botdiril.util.BotdirilFmt;

public abstract class ItemCrate extends Item implements IOpenable
{
    public static final int DISPLAY_LIMIT = 20;

    public ItemCrate(String name, String icon, String localizedName, String description)
    {
        super(name, icon, localizedName, description);
    }

    @Override
    public String getFootnote(CommandContext co)
    {
        if (co instanceof ChatCommandContext ccc)
            return "Open using `" + ccc.usedPrefix + "open " + this.getName() + "`. Keep in mind you need a key to do so.";


        return "Open using a key.";
    }

    @Override
    public boolean requiresKey()
    {
        return true;
    }

    protected abstract void addDrops(CommandContext co, ItemDrops id);

    public int getDisplayLimit()
    {
        return DISPLAY_LIMIT;
    }

    @Override
    public void open(CommandContext co, long amount)
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

            co.inventory.addItem(item, amt);

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

        co.userProperties.addStat(EnumStat.CRATES_OPENED, amount);

        co.respond(sb.toString());
    }
}
