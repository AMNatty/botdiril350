package com.botdiril.framework.command.parser;

import com.botdiril.discord.framework.DiscordEntityPlayer;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.discord.framework.util.DiscordAssert;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.EnumCommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.userdata.IIdentifiable;
import com.botdiril.userdata.achievement.Achievement;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.item.ItemCurrency;
import com.botdiril.userdata.item.ShopEntries;
import com.botdiril.userdata.items.Items;
import net.dv8tion.jda.api.entities.*;
import org.apache.commons.lang3.EnumUtils;

import java.util.stream.Collectors;

public class CommandParserTypeHandler
{
    private static ItemCurrency mapCurrency(ParType type)
    {
        return switch (type)
        {
            case AMOUNT_COINS -> Items.coins;
            case AMOUNT_CLASSIC_KEKS -> Items.keks;
            case AMOUNT_MEGA_KEKS -> Items.megakeks;
            case AMOUNT_KEK_TOKENS -> Items.tokens;
            case AMOUNT_DUST -> Items.dust;
            case AMOUNT_KEYS -> Items.keys;
            default -> throw new CommandException("Internal error. Please contact an administrator. Code: **NOT_A_CURRENCY**");
        };
    }

    private static long parseAmount(CommandContext co, Object[] argArr, int i, String arg)
    {
        if (i <= 1)
            throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM**");

        var prevArg = argArr[i - 1];

        if (prevArg instanceof Card)
        {
            return CommandAssert.parseAmount(arg, co.inventory.howManyOf((Card) prevArg), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half.");
        }
        else if (prevArg instanceof Item)
        {
            return CommandAssert.parseAmount(arg, co.inventory.howManyOf((Item) prevArg), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half.");
        }

        throw new CommandException("Internal error. Please contact an administrator. Code: **PREV_PARAM_NEITHER_CARD_OR_ITEM**");
    }

    private static long parseAmountBuy(CommandContext co, Object[] argArr, ParType type, int i, String arg)
    {
        if (i == 1)
            throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM**");

        var prevArg = argArr[i - 1];

        if (!(prevArg instanceof Item))
            throw new CommandException("Internal error. Please contact an administrator. Code: **NO_PREV_PARAM_NOT_ITEM**");

        var item = (Item) argArr[i - 1];

        return switch (type)
        {
            case AMOUNT_ITEM_BUY_COINS -> {
                if (!ShopEntries.canBeBought(item))
                    throw new CommandException("That item cannot be bought.");

                yield CommandAssert.parseBuy(arg, ShopEntries.getCoinPrice(item), co.inventory.getCoins(), "Could not parse the amount you are trying to buy.");
            }

            case AMOUNT_ITEM_BUY_TOKENS -> {
                if (!ShopEntries.canBeBoughtForTokens(item))
                    throw new CommandException("That item cannot be bought.");

                yield CommandAssert.parseBuy(arg, ShopEntries.getTokenPrice(item), co.inventory.getKekTokens(), "Could not parse the amount you are trying to buy.");
            }

            default -> throw new CommandException("Internal error. Please contact an administrator. Code: **BUY_UNKNOWN_CURRENCY**");
        };
    }

    public static <T, E extends Enum<E>> Object handleType(CommandContext co, Class<T> clazz, Object[] argArr, ParType type, int i, String arg)
    {
        if (clazz == int.class || clazz == Integer.class)
        {
            return CommandAssert.parseInt(arg, "Error: %s is not a valid number.".formatted(arg));
        }
        else if (clazz == boolean.class || clazz == Boolean.class)
        {
            return CommandAssert.parseBoolean(arg, "Error: %s is not a valid on/off state.".formatted(arg));
        }
        else if (clazz == long.class || clazz == Long.class)
        {
            return switch (type)
            {
                case BASIC ->
                    CommandAssert.parseLong(arg, String.format("Error: '%s' is not a valid number.", arg));

                case AMOUNT_COINS, AMOUNT_CLASSIC_KEKS, AMOUNT_MEGA_KEKS, AMOUNT_KEK_TOKENS, AMOUNT_DUST, AMOUNT_KEYS ->
                    CommandAssert.parseAmount(arg, co.inventory.howManyOf(mapCurrency(type)), "Amount could not be parsed, you can either use absolute numbers (0, 1, 2, 3, ...), percent (65%) or everything/half.");

                case AMOUNT_ITEM_OR_CARD ->
                    parseAmount(co, argArr, i, arg);

                case AMOUNT_ITEM_BUY_COINS, AMOUNT_ITEM_BUY_TOKENS ->
                    parseAmountBuy(co, argArr, type, i, arg);

                default -> throw new CommandException("Internal error. Please contact an administrator. Code: **UNEXPECTED_PAR_TYPE**");
            };
        }
        else if (clazz == IIdentifiable.class && type == ParType.ITEM_OR_CARD)
        {
            return CommandAssert.parseItemOrCard(arg);
        }
        else if (clazz == Item.class)
        {
            return CommandAssert.parseItem(arg);
        }
        else if (clazz == Card.class)
        {
            return CommandAssert.parseCard(arg);
        }
        else if (clazz == Achievement.class)
        {
            return CommandAssert.parseAchievement(arg);
        }
        else if (clazz == Command.class)
        {
            return CommandAssert.parseCommand(arg);
        }
        else if (clazz == EnumCommandCategory.class)
        {
            return CommandAssert.parseCommandGroup(arg);
        }
        else if (clazz == String.class)
        {
            return arg;
        }
        else if (clazz.isEnum())
        {
            @SuppressWarnings("unchecked")
            var enumClass = (Class<E>) clazz;
            var val = EnumUtils.getEnumIgnoreCase(enumClass, arg);

            if (val == null)
            {
                throw new CommandException(arg + " is not valid here. Try one of these: `" + EnumUtils.getEnumList(enumClass).stream().map(Enum::toString).map(String::toLowerCase).collect(Collectors.joining(", ")) + "`");
            }

            return val;
        }

        if (co instanceof DiscordCommandContext dcc)
        {
            if (clazz.isAssignableFrom(DiscordEntityPlayer.class) && (type == ParType.ENTITY_NOT_SELF || type == ParType.BASIC))
            {
                var player = DiscordAssert.parsePlayer(dcc.db, dcc.jda, dcc.guild, arg);

                if (type == ParType.ENTITY_NOT_SELF && dcc.player.equals(player))
                    throw new CommandException("You cannot use this command on yourself.");

                return player;
            }
            else if (clazz == User.class)
            {
                return DiscordAssert.parseUser(dcc.jda, arg);
            }
            else if (clazz == Role.class)
            {
                return DiscordAssert.parseRole(dcc.guild, arg);
            }
            else if (clazz == Member.class)
            {
                return DiscordAssert.parseMember(dcc.guild, arg);
            }
            else if (clazz == Emote.class)
            {
                return DiscordAssert.parseEmote(dcc.jda, arg);
            }
            else if (clazz == TextChannel.class)
            {
                return DiscordAssert.parseTextChannel(dcc.guild, arg);
            }
        }

        throw new CommandException("Internal error. Please contact an administrator. Code: **UNEXPECTED_PAR_CLASS**");
    }
}
