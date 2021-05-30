package com.botdiril.util;

import org.apache.commons.math3.random.RandomDataGenerator;

import java.util.List;
import java.util.Random;

public class BotdirilRnd
{
    public static final ThreadLocal<RandomDataGenerator> RDG = ThreadLocal.withInitial(RandomDataGenerator::new);

    public static final ThreadLocal<Random> RANDOM = ThreadLocal.withInitial(BotdirilRnd::createSeeded);

    private static final Random SEED_SOURCE = new Random();

    private static synchronized Random createSeeded()
    {
        return new Random(SEED_SOURCE.nextLong());
    }

    public static RandomDataGenerator rdg()
    {
        return RDG.get();
    }

    public static Random random()
    {
        return RANDOM.get();
    }

    public static boolean rollChance(RandomDataGenerator rdg, double chance)
    {
        return rdg.nextUniform(0, 1, true) < chance;
    }

    public static int rollDie(RandomDataGenerator rdg, int sides)
    {
        return rdg.nextInt(1, sides);
    }

    public static <E, T extends List<E>> E choose(Random random, T collection)
    {
        return collection.get(random.nextInt(collection.size()));
    }

    public static <E> E choose(Random random, E[] array)
    {
        return array[random.nextInt(array.length)];
    }

    public static boolean rollChance(double chance)
    {
        return rollChance(rdg(), chance);
    }

    public static int rollDie(int sides)
    {
        return rollDie(rdg(), sides);
    }

    public static <E, T extends List<E>> E choose(T collection)
    {
        return choose(random(), collection);
    }

    public static <E> E choose(E[] array)
    {
        return choose(random(), array);
    }
}
