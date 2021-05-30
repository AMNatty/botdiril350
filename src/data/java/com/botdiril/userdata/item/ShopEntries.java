package com.botdiril.userdata.item;

import com.botdiril.userdata.IGameObject;

import java.util.HashMap;
import java.util.Map;

public class ShopEntries
{
    private static final Map<Integer, Long> buysCoins = new HashMap<>();
    private static final Map<Integer, Long> sellsCoins = new HashMap<>();
    private static final Map<Integer, Long> buysTokens = new HashMap<>();

    private static final Map<Integer, Long> yieldsDust = new HashMap<>();

    // ADDING

    public static void addCoinBuy(Item item, long amount)
    {
        buysCoins.put(item.getID(), amount);
    }

    public static void addCoinSell(Item item, long amount)
    {
        sellsCoins.put(item.getID(), amount);
    }

    public static void addDisenchant(IGameObject item, long amount)
    {
        yieldsDust.put(item.getID(), amount);
    }

    public static void addTokenBuy(IGameObject item, long amount)
    {
        buysTokens.put(item.getID(), amount);
    }

    // CHECKING

    public static boolean canBeBought(Item item)
    {
        return buysCoins.containsKey(item.getID());
    }

    public static boolean canBeSold(Item item)
    {
        return sellsCoins.containsKey(item.getID());
    }

    public static boolean canBeBoughtForTokens(IGameObject item)
    {
        return buysTokens.containsKey(item.getID());
    }

    public static boolean canBeDisenchanted(IGameObject item)
    {
        return yieldsDust.containsKey(item.getID());
    }

    // RETRIEVING

    public static Long getCoinPrice(IGameObject item)
    {
        return buysCoins.get(item.getID());
    }

    public static Long getDustForDisenchanting(IGameObject item)
    {
        return yieldsDust.get(item.getID());
    }

    public static Long getSellValue(IGameObject item)
    {
        return sellsCoins.get(item.getID());
    }

    public static Long getTokenPrice(IGameObject item)
    {
        return buysTokens.get(item.getID());
    }
}
