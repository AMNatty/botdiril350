package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.CardDrops;
import com.botdiril.userdata.pools.CardPools;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.userdata.xp.XPRewards;
import com.botdiril.util.BotdirilFmt;

import java.util.stream.Collectors;

@Command("draw")
public class CommandDraw
{
    @CmdInvoke
    public static void draw(CommandContext co)
    {
        TimerUtil.require(co.inventory, EnumTimer.DRAW, "You need to wait $ before drawing cards again.");

        var lc = new CardDrops();

        var xpRewards = XPRewards.getLevel(co.inventory.getLevel());
        var drawPotency = xpRewards.getDrawPotency();

        if (Curser.isCursed(co, EnumCurse.CURSE_OF_YASUO))
        {
            lc.addItem(Card.getCardByName("yasuo"), drawPotency);
        }
        else
        {
            var cp = CardPools.rareOrBetter.draw();

            lc.addItem(cp);

            for (int i = 1; i < drawPotency; i++)
            {
                lc.addItem(CardPools.basicToLimited.draw());
            }
        }

        var msg = String.format("You drew %s.", lc.stream().map(cd -> {
            var card = cd.getCard();
            var amount = cd.getAmount();

            if (amount > 1)
            {
                return BotdirilFmt.amountOfMD(amount, card);
            }
            else
            {
                return String.format("**%s**", card.inlineDescription());
            }
        }).collect(Collectors.joining(", ")));

        lc.each(co.inventory::addCard);

        co.respond(msg);
    }
}
