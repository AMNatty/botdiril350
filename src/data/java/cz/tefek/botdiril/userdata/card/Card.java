package cz.tefek.botdiril.userdata.card;

import java.util.*;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.ItemLookup;
import cz.tefek.botdiril.userdata.pools.CardPools;

public class Card implements IIdentifiable
{
    private static final Map<String, Card> cards = new HashMap<>();

    public static Collection<Card> cards()
    {
        return Collections.unmodifiableCollection(cards.values());
    }

    public static Card getCardByID(int id)
    {
        return cards.get(ItemLookup.getName(id));
    }

    public static Card getCardByName(String name)
    {
        return cards.get(name.toLowerCase(Locale.ROOT));
    }

    public static long getPrice(Card c, int level)
    {
        return Math.round(Math.pow(c.getCardRarity().getLevelPriceIncrease(), level) * c.getCardRarity().getBasePrice());
    }

    private final String name;
    private final String localizedName;

    private String description = "";

    private final CardSet cardSet;

    private final EnumCardRarity cardRarity;

    private String customImage = null;

    private String collection;

    private String collectionName;

    private final int id;

    public Card(CardSet cardCollection, EnumCardRarity cardRarity, String name, String localizedName)
    {
        this.name = name;
        this.localizedName = localizedName;
        this.cardSet = cardCollection;
        this.cardRarity = cardRarity;

        this.id = ItemLookup.make(this.name);
        cards.put(this.name, this);

        if (cardCollection.canDrop())
        {
            var pool = switch (this.cardRarity) {
                case BASIC -> CardPools.basic;
                case COMMON -> CardPools.common;
                case RARE -> CardPools.rare;
                case LEGACY -> CardPools.legacy;
                case LEGENDARY -> CardPools.legendary;
                case LEGACY_LEGENDARY -> CardPools.legacylegendary;
                case ULTIMATE -> CardPools.ultimate;
                case LIMITED -> CardPools.limited;
                case MYTHIC -> CardPools.mythical;
                case UNIQUE -> CardPools.unique;
            };

            pool.add(this);
        }
    }

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Card)
        {
            Card it = (Card) obj;

            return it.getID() == this.getID();
        }

        return false;
    }

    public EnumCardRarity getCardRarity()
    {
        return this.cardRarity;
    }

    public CardSet getCardSet()
    {
        return this.cardSet;
    }

    public String getCollection()
    {
        return this.collection;
    }

    public String getCollectionName()
    {
        return this.collectionName;
    }

    public String getCustomImage()
    {
        return this.customImage;
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    public String getFootnote(CallObj co)
    {
        return "";
    }

    @Override
    public String getIcon()
    {
        return this.cardRarity.getCardIcon();
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public String getLocalizedName()
    {
        return this.localizedName;
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    public boolean hasCollection()
    {
        return this.collection != null;
    }

    public boolean hasCustomImage()
    {
        return this.customImage != null;
    }

    @Override
    public int hashCode()
    {
        return 31 + this.getID();
    }

    @Override
    public String inlineDescription()
    {
        return this.getIcon() + " " + this.getLocalizedName();
    }

    public Card setCollection(String collection)
    {
        this.collection = collection;

        return this;
    }

    public Card setCollectionName(String collectionName)
    {
        this.collectionName = collectionName;

        return this;
    }

    public Card setCustomImage(String customImage)
    {
        this.customImage = customImage;

        return this;
    }

    public Card setDescription(String description)
    {
        this.description = description;

        return this;
    }
}
