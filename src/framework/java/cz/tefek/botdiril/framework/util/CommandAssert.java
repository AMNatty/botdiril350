package cz.tefek.botdiril.framework.util;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.regex.Pattern;

import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.CommandStorage;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.userdata.IIdentifiable;
import cz.tefek.botdiril.userdata.achievement.Achievement;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.item.Item;

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

    // LONGS

    public static void assertTrue(boolean b, String errorMessage) throws CommandException
    {
        if (!b)
        {
            throw new CommandException(errorMessage);
        }
    }

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

    public static Member parseMember(Guild g, String inputArg)
    {
        if (inputArg.isEmpty())
        {
            throw new CommandException("Member could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(inputArg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                try
                {
                    return  g.retrieveMemberById(id).complete();
                }
                catch (ErrorResponseException e)
                {
                    throw switch (e.getErrorResponse())
                    {
                        case UNKNOWN_USER -> new CommandException("Member could not be parsed: Could not find a user with that ID.");
                        case UNKNOWN_MEMBER -> new CommandException("Member could not be parsed: Could not find this user in this server.");
                        default -> e;
                    };
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Member could not be parsed: Could not parse the snowflake ID / mention.");
        }

        throw new CommandException("Member could not be parsed: Could not locate the snowflake ID / mention.");
    }

    public static Role parseRole(Guild g, String msg)
    {
        if (msg.isEmpty())
        {
            throw new CommandException("Role could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var rbn = g.getRolesByName(msg.trim(), true);

            if (rbn.size() == 1)
            {
                return rbn.get(0);
            }
            else if (rbn.size() > 1)
            {
                throw new CommandException("There is too many roles with that name, try mentioning the role or using its id.");
            }

            var m = Pattern.compile("[0-9]+").matcher(msg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var r = g.getRoleById(id);

                if (r != null)
                {
                    return r;
                }
                else
                {
                    throw new CommandException("Role could not be parsed: Could not find a role with that snowflake ID. The role has to be on **this** server.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Role could not be parsed: Could not parse the snowflake ID.");
        }

        throw new CommandException("Could not find a role with such ID or name. Try using a mention.");
    }

    public static TextChannel parseTextChannel(Guild g, String msg)
    {
        if (msg.isEmpty())
        {
            throw new CommandException("Text channel could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(msg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var tc = g.getTextChannelById(id);

                if (tc != null)
                {
                    return tc;
                }
                else
                {
                    throw new CommandException("Text channel could not be parsed: Could not find a channel with that snowflake ID.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Text channel could not be parsed: Could not parse the snowflake ID.");
        }

        throw new CommandException("Text channel could not be parsed: Could not locate the snowflake ID.");
    }

    public static User parseUser(JDA jda, String inputArg)
    {
        if (inputArg.isEmpty())
        {
            throw new CommandException("User could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(inputArg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                try
                {
                    return  jda.retrieveUserById(id).complete();
                }
                catch (ErrorResponseException e)
                {
                    if (e.getErrorResponse() == ErrorResponse.UNKNOWN_USER)
                        throw new CommandException("User could not be parsed: Could not find a user with that ID.");
                    else
                        throw e;
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("User could not be parsed: Could not parse the snowflake ID / mention.");
        }

        throw new CommandException("User could not be parsed: Could not locate the snowflake ID / mention.");
    }

    public static Emote parseEmote(JDA jda, String arg)
    {
        if (arg.isEmpty())
        {
            throw new CommandException("Emoji could not be parsed: The input string cannot be empty.");
        }

        try
        {
            var m = Pattern.compile("[0-9]+").matcher(arg);

            if (m.find())
            {
                var id = Long.parseLong(m.group());

                var emote = jda.getEmoteById(id);

                if (emote != null)
                {
                    return emote;
                }
                else
                {
                    throw new CommandException("Emoji could not be parsed: Could not find an emoji with that ID.");
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Emoji could not be parsed: Could not parse the snowflake ID.");
        }

        throw new CommandException("Emoji could not be parsed: Could not locate the snowflake ID.");
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
