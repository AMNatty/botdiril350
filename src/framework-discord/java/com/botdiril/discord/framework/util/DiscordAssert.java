package com.botdiril.discord.framework.util;

import com.botdiril.discord.framework.DiscordEntityPlayer;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.sql.DBConnection;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.exceptions.ErrorResponseException;
import net.dv8tion.jda.api.requests.ErrorResponse;

import java.util.regex.Pattern;

public class DiscordAssert
{
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

    public static DiscordEntityPlayer parsePlayer(DBConnection db, JDA jda, Guild guild, String inputArg)
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
                    var user = jda.retrieveUserById(id).complete();

                    try
                    {
                        var member = guild.retrieveMember(user).complete();

                        return new DiscordEntityPlayer(db, member);
                    }
                    catch (ErrorResponseException e)
                    {
                        if (e.getErrorResponse() != ErrorResponse.UNKNOWN_MEMBER)
                        {
                            throw e;
                        }

                        return new DiscordEntityPlayer(db, user);
                    }
                }
                catch (ErrorResponseException e)
                {
                    if (e.getErrorResponse() == ErrorResponse.UNKNOWN_USER)
                    {
                        throw new CommandException("Member could not be parsed: Could not find a user with that ID.");
                    }

                    throw e;
                }
            }
        }
        catch (NumberFormatException e)
        {
            throw new CommandException("Member could not be parsed: Could not parse the snowflake ID / mention.");
        }

        throw new CommandException("Member could not be parsed: Could not locate the snowflake ID / mention.");
    }
}
