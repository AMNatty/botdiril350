package com.botdiril.command.inventory;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.userdata.card.Card;
import com.botdiril.userdata.card.CardDrops;
import com.botdiril.userdata.pools.CardPools;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.tempstat.EnumCurse;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.userdata.xp.XPRewards;

import java.util.stream.Collectors;

@Command(value = "draw", category = CommandCategory.ITEMS, levelLock = 5, description = "Draws some cards.")
public class CommandDraw
{
    @CmdInvoke
    public static void draw(CommandContext co)
    {
        TimerUtil.require(co.ui, EnumTimer.DRAW, "You need to wait $ before drawing cards again.");

        var lc = new CardDrops();

        var xpRewards = XPRewards.getLevel(co.ui.getLevel());
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
                return String.format("**%d %s**", amount, card.inlineDescription());
            }
            else
            {
                return String.format("**%s**", card.inlineDescription());
            }
        }).collect(Collectors.joining(", ")));

        lc.each(co.ui::addCard);

        co.respond(msg);
    }
}
