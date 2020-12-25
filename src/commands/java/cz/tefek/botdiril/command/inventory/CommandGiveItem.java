package cz.tefek.botdiril.command.inventory;

import net.dv8tion.jda.api.entities.User;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.command.invoke.ParType;
import cz.tefek.botdiril.userdata.IIdentifiable;

@Command(value = "give", aliases = { "giveitem", "givecard",
        "gift" }, category = CommandCategory.ITEMS, description = "Give someone an item or a card.", levelLock = 5)
public class CommandGiveItem
{
    @CmdInvoke
    public static void give(CallObj co, @CmdPar("user") User recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item, @CmdPar(value = "amount", type = ParType.AMOUNT_ITEM_OR_CARD) long amount)
    {
        throw new CommandException("Sorry, gifting has been temporarily disabled to prevent abuse.");

        /*
        CommandAssert.numberMoreThanZeroL(amount, "You can't give zero items.");

        if (recipient.getIdLong() == co.caller.getIdLong())
        {
            co.respond(String.format("You gave yourself **%d** %s(s)? ~~If there was a point in giving yourself your own stuff or something...~~", amount, item.inlineDescription()));
        }
        else
        {
            co.po.incrementStat(EnumStat.GIFTS_SENT);

            var eb = new EmbedBuilder();
            eb.setAuthor(co.caller.getName() + "#" + co.caller.getDiscriminator(), null, co.caller.getEffectiveAvatarUrl());
            eb.setTitle("Gift");

            if (Curser.isCursed(co, EnumCurse.HALVED_SELL_VALUE) && BotdirilRnd.RDG.nextUniform(0, 1) > 0.5)
            {
                eb.setTitle("Yoink!");
                recipient = co.bot;
            }

            var recipientUI = new UserInventory(co.db, recipient.getIdLong());

            if (item instanceof Item)
            {
                co.ui.addItem((Item) item, -amount);
                recipientUI.addItem((Item) item, amount);
            }
            else if (item instanceof Card)
            {
                co.ui.addCard((Card) item, -amount);
                recipientUI.addCard((Card) item, amount);
            }

            eb.setDescription(String.format("You gave **%s** **%d** **%s**.", recipient.getAsMention(), amount, item.inlineDescription()));
            eb.setThumbnail(recipient.getEffectiveAvatarUrl());
            eb.setColor(0x008080);

            co.respond(eb.build());
        }
        */
    }

    @CmdInvoke
    public static void giveOne(CallObj co, @CmdPar("user") User recipient, @CmdPar(value = "item or card", type = ParType.ITEM_OR_CARD) IIdentifiable item)
    {
        throw new CommandException("Sorry, gifting has been temporarily disabled to prevent abuse.");

        /*
        if (item instanceof Item)
        {
            CommandAssert.numberMoreThanZeroL(co.ui.howManyOf((Item) item), "You don't have that item to give it to someone.");
        }
        else if (item instanceof Card)
        {
            CommandAssert.numberMoreThanZeroL(co.ui.howManyOf((Card) item), "You don't have that item to give it to someone.");
        }

        give(co, recipient, item, 1);
         */
    }
}
