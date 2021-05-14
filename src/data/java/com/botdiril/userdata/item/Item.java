package com.botdiril.userdata.item;

import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.ItemLookup;
import com.botdiril.util.BotdirilLog;

import java.util.*;

public class Item implements IIdentifiable
{
    private static final Map<String, Item> items = new HashMap<>();

    public static Item getItemByID(int id)
    {
        return items.get(ItemLookup.getName(id));
    }

    public static Item getItemByName(String name)
    {
        return items.get(name.toLowerCase(Locale.ROOT));
    }

    public static Collection<Item> items()
    {
        return Collections.unmodifiableCollection(items.values());
    }

    private final String icon;

    private final String name;

    private final String localizedName;

    private final String description;

    private final int id;

    public Item(String name, String icon, String localizedName, String description)
    {
        this.name = name;
        this.icon = icon;
        this.localizedName = localizedName;
        this.description = description;

        this.id = ItemLookup.make(this.name);
        items.put(this.name, this);
    }

    public Item(String name, String icon, String localizedName)
    {
        this(name, icon, localizedName, "");
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    public String getFootnote(CommandContext co)
    {
        return "";
    }

    @Override
    public String getIcon()
    {
        if (this.icon == null)
        {
            BotdirilLog.logger.error("Every Item MUST have an icon. " + this.localizedName);
        }

        return this.icon;
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

    @Override
    public boolean equals(Object obj)
    {
        if (obj instanceof Item it)
            return it.getID() == this.getID();

        return false;
    }

    @Override
    public int hashCode()
    {
        return this.getID();
    }

    @Override
    public String inlineDescription()
    {
        return this.getIcon() + " " + this.getLocalizedName();
    }

    @Override
    public String toString()
    {
        return this.inlineDescription();
    }
}
