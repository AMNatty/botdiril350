package com.botdiril.userdata.item;

import com.botdiril.userdata.pools.PoolDrawer;
import com.botdiril.util.BotdirilFmt;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Iterable storage for item loot.
 * 
 */
public class ItemDrops implements Iterable<ItemPair>
{
    private final Map<Item, Long> lootMap = new HashMap<>();

    public void add(ItemDrops drops)
    {
        drops.lootMap.forEach((item, amount) -> lootMap.merge(item, amount, Long::sum));
    }

    public void addItem(Item item)
    {
        this.addItem(item, 1);
    }

    public void addItem(Item item, long amount)
    {
        if (amount == 0)
            return;

        this.lootMap.merge(item, amount, Long::sum);
    }

    public void addFromPool(PoolDrawer<Item> drawer)
    {
        this.addItem(drawer.draw(), 1);
    }

    public void addFromPool(PoolDrawer<Item> drawer, long amount)
    {
        for (int i = 0; i < amount; i++)
            this.lootMap.merge(drawer.draw(), 1L, Long::sum);
    }

    public int distintCount()
    {
        return this.lootMap.size();
    }

    public boolean hasItemDropped(Item item)
    {
        return this.lootMap.containsKey(item);
    }

    @Override
    public @NotNull Iterator<ItemPair> iterator()
    {
        return this.lootMap.entrySet().stream().map(entry -> ItemPair.of(entry.getKey(), entry.getValue())).iterator();
    }

    public long totalCount()
    {
        return this.lootMap.values().stream().mapToLong(Long::valueOf).sum();
    }

    public Stream<ItemPair> stream()
    {
        return this.lootMap.entrySet().stream().map(entry -> ItemPair.of(entry.getKey(), entry.getValue()));
    }

    public void each(BiConsumer<Item, Long> consumer)
    {
        this.lootMap.forEach(consumer);
    }

    public String toStringJoined(String joiner)
    {
        return this.lootMap.entrySet().stream().map(e -> BotdirilFmt.amountOf(e.getValue(), e.getKey())).collect(Collectors.joining(joiner));
    }

    @Override
    public String toString()
    {
        return this.toStringJoined(", ");
    }
}
