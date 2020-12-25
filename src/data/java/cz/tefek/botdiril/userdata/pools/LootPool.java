package cz.tefek.botdiril.userdata.pools;

import java.util.ArrayList;

import cz.tefek.botdiril.util.BotdirilRnd;

public class LootPool<E> extends ArrayList<E>
{
    public E draw()
    {
        return BotdirilRnd.choose(this);
    }
}
