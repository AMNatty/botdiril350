package com.botdiril.userdata.items.cardpack;

import com.botdiril.userdata.pools.CardPools;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.util.BotdirilRnd;

public class CardPacks
{
    public static ItemCardPack cardPackBasic;
    public static ItemCardPack cardPackNormal;
    public static ItemCardPack cardPackGood;
    public static ItemCardPack cardPackVoid;

    public static void load()
    {
        cardPackBasic = new ItemCardPackSimple("basiccardpack", Icons.CARDPACK_BASIC, "Basic Card Pack",
            CardPools.basicOrCommon, 10, "Contains all the essential cards for your collections.");
        ShopEntries.addCoinSell(cardPackBasic, 1000);

        cardPackNormal = new ItemCardPackSimple("cardpack", Icons.CARDPACK_NORMAL, "Card Pack",
            CardPools.basicToLimited, 8, "Contains a variety of cards ranging from basic and common to mythical and limited cards.");

        cardPackGood = new ItemCardPackSimple("goodcardpack", Icons.CARDPACK_GOOD, "Good Card Pack",
            CardPools.rareOrBetter, 8, "For the true collectors, drops legacy/rare cards or better.");

        cardPackVoid = new ItemCardPackSimple("voidcardpack", Icons.CARDPACK_VOID, "Void Card Pack",
            CardPools.rareOrBetterV, 12, (co, amount) -> {
            final double CURSE_CHANCE = 0.2;

            for (int i = 0; i < amount; i++)
                if (BotdirilRnd.RDG.nextUniform(0, 1) < CURSE_CHANCE)
                    Curser.curse(co);

        }, "For a very dangerous place Void is, it contains some *awesome* loot. Open this pack at your own risk. It may turn into a Pandora's box.");
    }
}
