package cz.tefek.botdiril.userdata.card;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

/**
 * Iterable storage for card loot.
 * 
 */
public class CardDrops implements Iterable<CardPair>
{
    private final Map<Card, Long> lootMap = new HashMap<>();

    public void addItem(Card card)
    {
        this.addItem(card, 1);
    }

    public void addItem(Card card, long amount)
    {
        this.lootMap.merge(card, amount, Long::sum);
    }

    public int distintCount()
    {
        return this.lootMap.size();
    }

    public boolean hasItemDropped(Card item)
    {
        return this.lootMap.containsKey(item);
    }

    @Override
    public @NotNull Iterator<CardPair> iterator()
    {
        return this.lootMap.entrySet().stream().map(entry -> new CardPair(entry.getKey(), entry.getValue())).iterator();
    }

    public long totalCount()
    {
        return this.lootMap.values().stream().mapToLong(Long::valueOf).sum();
    }

    public Stream<CardPair> stream()
    {
        return this.lootMap.entrySet().stream().map(entry -> new CardPair(entry.getKey(), entry.getValue()));
    }

    public void each(BiConsumer<Card, Long> consumer)
    {
        this.lootMap.forEach(consumer);
    }
}
