package com.botdiril.userdata.item;

import com.botdiril.userdata.IIdentifiable;

import java.util.List;

public record Recipe(
    List<ItemPair> components,
    long amount,
    IIdentifiable result
)
{

}
