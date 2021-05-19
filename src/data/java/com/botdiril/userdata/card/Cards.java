package com.botdiril.userdata.card;

import com.botdiril.MajorFailureException;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.Recipe;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.userdata.items.Items;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class Cards
{
    public static CardSet league;

    private record CardSkin(
        String name,
        String id,
        EnumCardRarity rarity
    )
    {

    }

    private record CardItem(
        String id,
        String name,
        List<CardSkin> skins
    )
    {

    }

    private record CardSkinData(
        String setName,
        String set,
        boolean drops,
        String description,
        String idPrefix,
        List<CardItem> items
    )
    {

    }

    public static void load()
    {
        try (var br = Files.newBufferedReader(Path.of("assets", "cardSets", "lolskindata-g.json")))
        {
            var mapper = new ObjectMapper();
            var skinData = mapper.readValue(br, CardSkinData.class);

            league = new CardSet(skinData.set(), skinData.setName(), skinData.idPrefix(), skinData.drops(), skinData.description());

            skinData.items().forEach(cardItem ->
                cardItem.skins().forEach(cardSkin -> {
                    var collID = cardSkin.id();
                    var collName = cardSkin.name();
                    var rarity = cardSkin.rarity();

                    var cc = new Card(league, rarity, collID, collName);
                    var dustValue = rarity.getBasePrice();
                    ShopEntries.addDisenchant(cc, dustValue);
                    CraftingEntries.add(new Recipe(List.of(Items.dust.ofAmount(dustValue * 10)), 1, cc));
                    cc.setCollection(collID);
                    cc.setCollectionName(collName);
            }));
        }
        catch (Exception e)
        {
            throw new MajorFailureException("League of Legends skin data not found or malformed! Aborting.", e);
        }
    }
}
