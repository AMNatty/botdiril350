package com.botdiril.gamelogic.weighted;

import com.botdiril.MajorFailureException;
import com.botdiril.util.BotdirilRnd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

        var ec = enumClass.getEnumConstants();
        var cumulative = 0.0;

        do
        {
            var rolledValue = BotdirilRnd.RDG.nextUniform(0, getCumulativeWeight(enumClass));

            for (var enumConstant : ec)
            {
                cumulative += enumConstant.getWeight();

                if (cumulative >= rolledValue)
                {
                    if (!enumConstant.isApplicable())
                        break;

                    return enumConstant;
                }
            }
        }
        while (Arrays.stream(ec).anyMatch(IWeightedRandom::isApplicable));

        return ec[ec.length - 1];
    }

    double getWeight();

    default boolean isApplicable()
    {
        return true;
    }

    @SuppressWarnings("unchecked")
    default double getActualWeight()
    {
        return this.getWeight() / getCumulativeWeight((Class<E>) this.getClass());
    }
}
