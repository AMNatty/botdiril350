package com.botdiril.gamelogic.weighted;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.botdiril.MajorFailureException;
import com.botdiril.util.BotdirilRnd;

public interface IWeightedRandom<E extends Enum<E> & IWeightedRandom<E>>
{
    Map<Class<? extends IWeightedRandom<?>>, Double> cumulativeWeightCache = new HashMap<>();

    static <EC extends Enum<EC> & IWeightedRandom<EC>> double getCumulativeWeight(Class<EC> enumClass)
    {
        if (!enumClass.isEnum())
            throw new MajorFailureException("Classes implementing IWeighted must be enum classes!");

        cumulativeWeightCache.computeIfAbsent(enumClass, iwc -> {
            var ec = enumClass.getEnumConstants();
            return Arrays.stream(ec).mapToDouble(IWeightedRandom::getWeight).sum();
        });

        return cumulativeWeightCache.get(enumClass);
    }

    static <EC extends Enum<EC> & IWeightedRandom<EC>> EC choose(Class<EC> enumClass)
    {
        if (!enumClass.isEnum())
            throw new MajorFailureException("Classes implementing IWeighted must be enum classes!");

        var value = BotdirilRnd.RDG.nextUniform(0, getCumulativeWeight(enumClass));
        var ec = enumClass.getEnumConstants();
        var cumulative = 0.0;

        for (var enumConstant : ec)
        {
            cumulative += enumConstant.getWeight();

            if (value <= cumulative)
                return enumConstant;
        }

        return ec[ec.length - 1];
    }

    double getWeight();

    @SuppressWarnings("unchecked")
    default double getActualWeight()
    {
        return this.getWeight() / getCumulativeWeight((Class<E>) this.getClass());
    }
}
