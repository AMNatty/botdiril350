package com.botdiril.framework.util;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandStorage;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.Item;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

public class CommandAssert
{
    public static void assertNotNull(Object o1, String errorMessage) throws CommandException
    {
        if (o1 == null)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void assertEquals(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (!o1.equals(o2))
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void assertIdentity(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (o1 != o2)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void assertNotEquals(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (o1.equals(o2))
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void assertNotIdentity(Object o1, Object o2, String errorMessage) throws CommandException
    {
        if (o1 == o2)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void assertTrue(boolean b, String errorMessage) throws CommandException
    {
        if (!b)
        {
            throw new CommandException(errorMessage);
        }
    }

    // LONGS

    public static void matchesRegex(String s, String regex, String errorMessage) throws CommandException
    {
        if (s.matches(regex))
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberInBoundsExclusiveD(double number, double levelMin, double levelMax, String errorMessage) throws CommandException
    {
        if (number <= levelMin || number >= levelMax)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberInBoundsExclusiveL(long number, long levelMin, long levelMax, String errorMessage) throws CommandException
    {
        if (number <= levelMin || number >= levelMax)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberInBoundsInclusiveD(double number, double levelMin, double levelMax, String errorMessage) throws CommandException
    {
        if (number < levelMin || number > levelMax)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberInBoundsInclusiveL(long number, long levelMin, long levelMax, String errorMessage) throws CommandException
    {
        if (number < levelMin || number > levelMax)
        {
            throw new CommandException(errorMessage);
        }
    }

    // DOUBLES

    public static void numberMoreThanZeroD(double number, String errorMessage) throws CommandException
    {
        if (!(number > 0))
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberMoreThanZeroL(long number, String errorMessage) throws CommandException
    {
        if (!(number > 0))
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberNotAboveD(double number, double level, String errorMessage) throws CommandException
    {
        if (number > level)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberNotAboveL(long number, long level, String errorMessage) throws CommandException
    {
        if (number > level)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberNotBelowD(double number, double level, String errorMessage) throws CommandException
    {
        if (number < level)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void numberNotBelowL(long number, long level, String errorMessage) throws CommandException
    {
        if (number < level)
        {
            throw new CommandException(errorMessage);
        }
    }

    // STRINGS

    public static Achievement parseAchievement(String name)
    {
        var it = Achievement.getByName(name);

        if (it == null)
        {
            throw new CommandException("Achievement with that name was not found.");
        }

        return it;
    }

    public static long parseAmount(String amountString, long base, String errorMessage) throws CommandException
    {
        long amount;

        try
        {
            amount = Long.parseUnsignedLong(amountString);

            if (amount > base)
            {
                throw new CommandException("That's more than you have!");
            }
        }
        catch (NumberFormatException e)
        {
            if (amountString.endsWith("%"))
            {
                try
                {
                    double f = Float.parseFloat(amountString.substring(0, amountString.length() - 1));

                    if (f < 0 || f > 100)
                    {
                        throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                    }
                    else
                    {
                        amount = Math.round(f / 100.0 * base);
                    }
                }
                catch (NumberFormatException e1)
                {
                    throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                }
            }
            else
            {
                if (amountString.equalsIgnoreCase("all") || amountString.equalsIgnoreCase("everything") || amountString.equalsIgnoreCase("max"))
                {
                    amount = base;
                }
                else if (amountString.equalsIgnoreCase("half"))
                {
                    amount = base / 2L;
                }
                else if (amountString.equalsIgnoreCase("keepone"))
                {
                    amount = base > 1 ? base - 1 : 0;
                }
                else
                {
                    throw new CommandException(errorMessage);
                }
            }
        }

        return amount;
    }

    // PARSERS

    public static long parseBuy(String amountString, long price, long money, String errorMessage) throws CommandException
    {
        long amount;

        try
        {
            amount = Long.parseUnsignedLong(amountString);

            if (amount * price > money)
            {
                throw new CommandException("That's more than you can afford!");
            }
        }
        catch (NumberFormatException e)
        {
            if (amountString.endsWith("%"))
            {
                try
                {
                    double f = Float.parseFloat(amountString.substring(0, amountString.length() - 1));

                    if (f < 0 || f > 100)
                    {
                        throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                    }
                    else
                    {
                        amount = Math.round(f / 100.0 * money / price);
                    }
                }
                catch (NumberFormatException e1)
                {
                    throw new CommandException(errorMessage + "\nThis is not a valid percentage.");
                }
            }
            else
            {
                if (amountString.equalsIgnoreCase("all"))
                {
                    amount = money / price;
                }
                else if (amountString.equalsIgnoreCase("half"))
                {
                    amount = money / price / 2;
                }
                else
                {
                    throw new CommandException(errorMessage);
                }
            }
        }

        return amount;
    }

    public static Command parseCommand(String arg)
    {
        var cmd = CommandStorage.search(arg);

        if (cmd == null)
        {
            throw new CommandException("No such command.");
        }
        else
        {
            return cmd;
        }
    }

    public static CommandCategory parseCommandGroup(String name)
    {
        var cg = Arrays.stream(CommandCategory.values()).filter(cc -> cc.toString().equalsIgnoreCase(name.trim())).findFirst();

        if (cg.isEmpty())
        {
            throw new CommandException("No such command group.");
        }

        return cg.get();
    }

    /**
     * Hard failing
     */
    public static double parseDouble(String number, String errorMessage) throws CommandException
    {
        try
        {
            return Double.parseDouble(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    /**
     * Hard failing
     */
    public static int parseInt(String number, String errorMessage) throws CommandException
    {
        try
        {
            return Integer.parseInt(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static boolean parseBoolean(String bool, String errorMessage)
    {
        if ("true".equalsIgnoreCase(bool) || "yes".equalsIgnoreCase(bool) || "1".equalsIgnoreCase(bool) || "on".equalsIgnoreCase(bool) || "enable".equalsIgnoreCase(bool))
        {
            return true;
        }
        else if ("false".equalsIgnoreCase(bool) || "no".equalsIgnoreCase(bool) || "0".equalsIgnoreCase(bool) || "off".equalsIgnoreCase(bool) || "disable".equalsIgnoreCase(bool))
        {
            return false;
        }
        else
        {
            throw new CommandException(errorMessage);
        }
    }

    public static Card parseCard(String cname)
    {
        var ct = Card.getCardByName(StringUtils.removeEnd(cname, "s"));

        if (ct != null)
        {
            return ct;
        }

        throw new CommandException("Card with that name was not found.");
    }


    public static Item parseItem(String name)
    {
        var it = Item.getItemByName(StringUtils.removeEnd(name, "s"));

        if (it != null)
        {
            return it;
        }

        throw new CommandException("Item with that name was not found.");
    }

    public static IIdentifiable parseItemOrCard(String name)
    {
        var it = Item.getItemByName(StringUtils.removeEnd(name, "s"));

        if (it != null)
        {
            return it;
        }

        var ct = Card.getCardByName(StringUtils.removeEnd(name, "s"));

        if (ct != null)
        {
            return ct;
        }

        throw new CommandException("Could not find card or an item with that ID.");
    }

    /**
     * Hard failing
     */
    public static long parseLong(String number, String errorMessage) throws CommandException
    {
        try
        {
            return Long.parseLong(number);
        }
        catch (NumberFormatException e)
        {
            throw new CommandException(errorMessage);
        }
    }

    public static void stringNotEmptyOrNull(String s, String errorMessage) throws CommandException
    {
        if (s == null)
        {
            throw new CommandException(errorMessage);
        }

        if (s.isEmpty())
        {
            throw new CommandException(errorMessage);
        }

    }

    public static void stringNotTooLong(String s, int length, String errorMessage) throws CommandException
    {
        if (s.length() > length)
        {
            throw new CommandException(errorMessage);
        }
    }
}
