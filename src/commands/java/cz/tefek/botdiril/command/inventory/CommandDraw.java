package cz.tefek.botdiril.command.inventory;

import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.util.CommandAssert;
import cz.tefek.botdiril.userdata.card.Card;
import cz.tefek.botdiril.userdata.card.CardDrops;
import cz.tefek.botdiril.userdata.pools.CardPools;
import cz.tefek.botdiril.userdata.tempstat.Curser;
import cz.tefek.botdiril.userdata.tempstat.EnumCurse;
import cz.tefek.botdiril.userdata.timers.Timers;
import cz.tefek.botdiril.userdata.xp.XPRewards;

@Command(value = "draw", category = CommandCategory.ITEMS, levelLock = 5, description = "Draws some cards.")
public class CommandDraw
{
    @CmdInvoke
    public static void draw(CallObj co)
    {
        CommandAssert.assertTimer(co.ui, Timers.draw, "You need to wait $ before drawing cards again.");

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
