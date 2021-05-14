package com.botdiril.userdata.pools;

public record PoolWrapper<T>(long weight, LootPool<T> pool)
{
}
