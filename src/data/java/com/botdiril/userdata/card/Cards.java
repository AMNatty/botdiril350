package com.botdiril.userdata.card;

import com.botdiril.MajorFailureException;
import com.botdiril.userdata.item.CraftingEntries;
import com.botdiril.userdata.item.ItemPair;
import com.botdiril.userdata.item.Recipe;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.userdata.items.Items;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileReader;
import java.util.List;

public class Cards
{
    public static void load()
    {
        try (var br = new FileReader("assets/cardSets/lolskindata-g.json"))
        {
            var jobj = new JSONObject(new JSONTokener(br));

            CardSet.league = new CardSet(jobj.getString("set"), jobj.getString("setName"), jobj.getString("idPrefix"), true, jobj.getString("description"));

            jobj.getJSONArray("items").forEach(ch ->
            {
                var jch = (JSONObject) ch;

                var collID = jch.getString("id");
                var collName = jch.getString("name");

                jch.getJSONArray("skins").forEach(chi ->
                {
                    var jchi = (JSONObject) chi;

                    var rarity = jchi.getEnum(EnumCardRarity.class, "rarity");
                    var cc = new Card(CardSet.league, rarity, jchi.getString("id"), jchi.getString("name"));
                    var dustValue = rarity.getBasePrice();
                    ShopEntries.addDisenchant(cc, dustValue);
                    CraftingEntries.add(new Recipe(List.of(ItemPair.of(Items.dust, dustValue * 10)), 1, cc));
                    cc.setCollection(collID);
                    cc.setCollectionName(collName);
                });
            });
        }
        catch (Exception e)
        {
            throw new MajorFailureException("League of Legends skin data not found or malformed! Aborting.", e);
        }

        /*
        try (var br = new FileReader("assets/cardSets/csgo-g.json"))
        {
            var jobj = new JSONObject(new JSONTokener(br));

            CardSet.csgo = new CardSet(jobj.getString("set"), jobj.getString("setName"), jobj.getString("idPrefix"));
            CardSet.csgo.setDescription(jobj.getString("description"));

            jobj.getJSONArray("items").forEach(ch ->
            {
                var jch = (JSONObject) ch;

                var rarity = jch.getEnum(EnumCardRarity.class, "rarity");
                var cc = new Card(CardSet.csgo, rarity, jch.getString("id"), jch.getString("name"));
                ShopEntries.addCoinSell(cc, rarity.getBasePrice());
                ShopEntries.addDisenchant(cc, rarity.getBasePrice() * 100);
                CraftingEntries.add(new Recipe(List.of(ItemPair.of(Items.dust, rarity.getBasePrice() * 3 * 100)), 1, cc));
                cc.setCustomImage(jch.getString("iconurl"));
            });
        }
        catch (Exception e)
        {
            throw new MajorFailureException("CS:GO skin data not found or malformed! Aborting.", e);
        }

        try (var br = new FileReader("assets/cardSets/terraria-g.json"))
        {
            var jobj = new JSONObject(new JSONTokener(br));
        
            CardSet.terraria = new CardSet(jobj.getString("set"), jobj.getString("setName"), jobj.getString("idPrefix"));
            CardSet.terraria.setDescription(jobj.getString("description"));
        
            jobj.getJSONArray("items").forEach(ch ->
            {
                var jch = (JSONObject) ch;
        
                var rarity = jch.getEnum(EnumCardRarity.class, "rarity");
                var cc = new Card(CardSet.csgo, rarity, jch.getString("id"), jch.getString("name"));
                ShopEntries.addCoinSell(cc, rarity.getBasePrice());
                ShopEntries.addDisenchant(cc, rarity.getBasePrice() * 100);
                CraftingEntries.add(new Recipe(Arrays.asList(new ItemPair(Items.dust, rarity.getBasePrice() * 3 * 100)), 1, cc));
            });
        }
        catch (Exception e)
        {
            BotMain.logger.fatal("Terraria skin data not found or malformed! Aborting.", e);
            System.exit(11);
        }
        */
    }
}
