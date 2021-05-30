package com.botdiril.userdata;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

public interface INamedObject extends Comparable<INamedObject>
{
    Comparator<INamedObject> DEFAULT_COMPARATOR = Comparator.comparingInt(INamedObject::getID);

    default String getPrefix()
    {
        return "";
    }

    String getName();

    int getID();

    @Override
    default int compareTo(@NotNull INamedObject o)
    {
        return DEFAULT_COMPARATOR.compare(this, o);
    }
}
