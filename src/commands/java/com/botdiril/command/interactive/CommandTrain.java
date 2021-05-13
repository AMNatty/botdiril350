package com.botdiril.command.interactive;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.command.invoke.ParType;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.gamelogic.card.TrainAPI;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.icon.IconUtil;
import com.botdiril.userdata.item.Item;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import net.dv8tion.jda.api.EmbedBuilder;

import com.botdiril.util.BotdirilFmt;

@Command(
    value = "train",
    category = CommandCategory.INTERACTIVE,
    levelLock = 10,
    aliases = { "traincard", "cardtrain" },
    description = "Spend some resources to train your card."
)
public class CommandTrain
{
    @CmdInvoke
    public static void train(CommandContext co)
    {
        var eb = new EmbedBuilder();
        eb.setTitle("Train a card");
        eb.setDescription("Spend a resource to train your card. This increases the value and power of all cards of that type.\n\n" +
                          "**Usage:** `%s%s <card to train> <item> [amount]`".formatted(co.usedPrefix, co.usedAlias));
        eb.setColor(0x008080);
        eb.setThumbnail(co.bot.getEffectiveAvatarUrl());

        var sbLeft = new StringBuilder();
        var sbRight = new StringBuilder();

        TrainAPI.TRAINING_ITEMS.forEach((item, value) -> {
            sbLeft.append(String.format("\n%s", item.inlineDescription()));
            sbRight.append(String.format("\n%s XP", BotdirilFmt.format(value)));
        });

        eb.addField("Possible training items", sbLeft.toString(), true);
        eb.addField("Estimate XP gain", sbRight.toString(), true);
        eb.setFooter("Please view this table on a desktop computer for optimal view.");

        co.respond(eb);
    }

    @CmdInvoke
    public static void train(CommandContext co, @CmdPar("card to train") Card card, @CmdPar("training item") Item item, @CmdPar(value = "amount of training item", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        CommandAssert.numberMoreThanZeroL(co.ui.howManyOf(card), "*You need to have at least one card of this kind to train it.*");

        TimerUtil.require(co.ui, EnumTimer.TRAIN, "You need to wait **$** before attempting to **train a card** again.");

        if (!TrainAPI.TRAINING_ITEMS.containsKey(item))
        {
            throw new CommandException("**%s** is not a valid training item, please refer to `%s%s` for more information.".formatted(item.inlineDescription(), co.usedPrefix, co.usedAlias));
        }

        co.ui.addItem(item, -amount);

        var result = TrainAPI.roll(item, amount);
        var outcome = result.getOutcome();

        var xp = result.getXPLearnt();

        var title = switch (outcome) {
            case BAD -> "Minor failure";
            case NORMAL -> "Average success";
            case GOOD -> "Minor success";
            case VERY_GOOD -> "Major success";
        };

        var description = switch (outcome) {
            case BAD -> "Your card tried its best, but it simply wasn't performing.";
            case NORMAL -> "Your card's performance was average, nothing extraordinary happened.";
            case GOOD -> "Your card performed well and even learnt some new tricks.";
            case VERY_GOOD -> "Your card has surpassed all your expectations. You feel really good about this.";
        };

        var eb = new EmbedBuilder();
        eb.setTitle(title);
        eb.setDescription(description);
        eb.setColor(0x008080);
        eb.setThumbnail(IconUtil.urlFromIcon(co.jda, card));
        eb.addField("XP received", BotdirilFmt.format(xp), true);
        eb.addField("Multiplier", BotdirilFmt.format(Math.round(outcome.getMultiplier() * 100)) + " %", true);

        co.respond(eb);

        co.ui.addCardXP(co, card, xp);
    }

    @CmdInvoke
    public static void train(CommandContext co, @CmdPar("card to train") Card card, @CmdPar("training item") Item item)
    {
        if (co.ui.howManyOf(item) < 1)
            throw new CommandException("*You don't have any **%s***".formatted(item.inlineDescription()));

        train(co, card, item, 1);
    }
}
