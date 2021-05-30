package com.botdiril.userdata.achievement;

import com.botdiril.userdata.IGameObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.botdiril.userdata.ItemLookup;

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
        return name;
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
        return description;
    }

    @Override
    public String getLocalizedName()
    {
        return localizedName;
    }

    @Override
    public String getInlineDescription()
    {
        return localizedName;
    }

    public static Achievement getByName(String name)
    {
        return storage.get(name.toLowerCase(Locale.ROOT));
    }
}
