package com.botdiril.userdata.pools;

public class PoolWrapper<T>
{
    private final long weight;
    private final LootPool<T> pool;

    public PoolWrapper(long weight, LootPool<T> pool)
    {
        this.weight = weight;
        this.pool = pool;
    }

    public long getWeight()
    {
        return weight;
    }

    public LootPool<T> getPool()
    {
        return pool;
    }
}
