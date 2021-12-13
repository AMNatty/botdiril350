package com.botdiril.userdata.achievement;

import com.botdiril.userdata.IGameObject;
import com.botdiril.userdata.ItemLookup;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Achievement implements IGameObject
{
    private final int id;
    private final String name;
    private final String localizedName;
    private final String description;
    private final String icon;

    private static final Map<String, Achievement> storage = new HashMap<>();

    public Achievement(String name, String localizedName, String description, String icon)
    {
        this.name = name;
        this.localizedName = localizedName;
        this.description = description;
        this.icon = icon;

        this.id = ItemLookup.make(this.name);
        storage.put(this.name, this);
    }

    @Override
    public String getName()
    {
        return this.name;
    }

    @Override
    public int getID()
    {
        return this.id;
    }

    @Override
    public String getIcon()
    {
        return this.icon;
    }

    @Override
    public String getDescription()
    {
        return this.description;
    }

    @Override
    public String getLocalizedName()
    {
        return this.localizedName;
    }

    @Override
    public String getInlineDescription()
    {
        return "%s %s".formatted(this.icon, this.localizedName);
    }

    public static Achievement getByName(String name)
    {
        return storage.get(name.toLowerCase(Locale.ROOT));
    }
}
