package com.botdiril.userdata;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface IIdentifiable extends Comparable<IIdentifiable>
{
    Comparator<IIdentifiable> DEFAULT_COMPARATOR = Comparator.comparingInt(IIdentifiable::getID);

    String getDescription();

    String getIcon();

    int getID();

    String getLocalizedName();

    String getName();

    default boolean hasIcon()
    {
        return this.getIcon() != null;
    }

    String inlineDescription();

    @Override
    default int compareTo(@NotNull IIdentifiable o)
    {
        return DEFAULT_COMPARATOR.compare(this, o);
    }
}
