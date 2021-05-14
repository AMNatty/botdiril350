package com.botdiril.command.inventory;

import com.botdiril.framework.EntityPlayer;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.InventoryTables;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemCurrency;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilLog;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Command(value = "inventory", aliases = { "inv",
        "i" }, category = CommandCategory.ITEMS, description = "Shows your/someone's inventory.")
public class CommandInventory
{
    private static final long ITEMS_PER_PAGE = 21L;

    private static final Comparator<ItemPair> valueComparator = (i1, i2) ->
    {
        if (!ShopEntries.canBeBought(i2.getItem()) && !ShopEntries.canBeBought(i1.getItem()))
        {
            return Integer.MIN_VALUE + 1;
        }

        if (!ShopEntries.canBeBought(i2.getItem()))
        {
            return Integer.MIN_VALUE + 1;
        }

        if (!ShopEntries.canBeBought(i1.getItem()))
        {
            return Integer.MAX_VALUE - 1;
        }

        return Long.compare(ShopEntries.getCoinPrice(i2.getItem()), ShopEntries.getCoinPrice(i1.getItem()));
    };

    private static List<ItemPair> getInventory(CommandContext co, EntityPlayer player)
    {
        var ui = player.inventory();

        return co.db.exec("SELECT * FROM " + InventoryTables.TABLE_INVENTORY + " WHERE fk_us_id=? AND it_amount>0", stat ->
        {
            var ips = new ArrayList<ItemPair>();
            var eq = stat.executeQuery();

            while (eq.next())
            {
                var ilID = eq.getInt("fk_il_id");
                var item = Item.getItemByID(ilID);

                if (item == null)
                {
                    BotdirilLog.logger.warn(String.format("User FID %d has a null item in their inventory! ID: %d", ui.getFID(), ilID));
                    continue;
                }

                if (item instanceof ItemCurrency)
                {
                    continue;
                }

                ips.add(ItemPair.of(item, eq.getLong("it_amount")));
            }

            return ips;
        }, ui.getFID());
    }

    @CmdInvoke
    public static void show(CommandContext co)
    {
        show(co, co.player);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("player") EntityPlayer player)
    {
        show(co, player, 1);
    }

    @CmdInvoke
    public static void show(CommandContext co, @CmdPar("player") EntityPlayer player, @CmdPar("page") long page)
    {
        var ips = getInventory(co, player);

        if (ips.isEmpty())
        {
            co.respond("The inventory is empty.");
            return;
        }

        CommandAssert.numberNotBelowL(page, 1, "Invalid page.");

        var eb = new ResponseEmbed();

        eb.setAuthor(player.getTag(), null, player.getAvatarURL());
        eb.setTitle("This user has " + BotdirilFmt.format(ips.size()) + " different types of items.");
        eb.setDescription(player.getMention() + "'s inventory.");
        eb.setColor(0x008080);
        eb.setThumbnail(player.getAvatarURL());

        var isc = ips.stream();

        var pageCount = 1 + (ips.size() - 1) / ITEMS_PER_PAGE;

        if (page > pageCount)
        {
            page = pageCount;
        }

        eb.appendDescription(String.format("\nPage %d/%d", page, pageCount));

        isc.sorted(valueComparator).skip((page - 1) * ITEMS_PER_PAGE).limit(ITEMS_PER_PAGE).forEach(ip ->
            eb.addField(ip.getItem().inlineDescription(), String.format("Amount: **%s**\nID: **%s**", BotdirilFmt.format(ip.getAmount()), ip.getItem().getName()), true));

        if (co instanceof ChatCommandContext ccc)
            eb.setFooter("Use `%s%s %s <page>` to go to another page.".formatted(ccc.usedPrefix, ccc.usedAlias, ccc.player.getMention()), null);

        co.respond(eb);
    }
}
