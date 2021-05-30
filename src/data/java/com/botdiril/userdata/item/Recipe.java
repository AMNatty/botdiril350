package com.botdiril.userdata.item;

import com.botdiril.userdata.IGameObject;

import java.util.List;

public record Recipe(
    List<ItemPair> components,
    long amount,
    IGameObject result
)
{

}
