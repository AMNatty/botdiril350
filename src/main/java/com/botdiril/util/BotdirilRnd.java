package com.botdiril.util;

import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.List;
import java.util.Random;

public class BotdirilRnd
{
    public static final RandomDataGenerator RDG = new RandomDataGenerator();

    public static final Random RANDOM = new Random();

    public static boolean rollChance(double chance)
    {
        return RDG.nextUniform(0, 1, true) < chance;
    }

    public static int rollDie(int sides)
    {
        return RDG.nextInt(1, sides);
    }

    public static <E, T extends List<E>> E choose(T collection)
    {
        return collection.get(RANDOM.nextInt(collection.size()));
    }

    public static <E> E choose(E[] array)
    {
        return array[RANDOM.nextInt(array.length)];
    }
}
