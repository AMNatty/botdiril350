package com.botdiril.command;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.gamelogic.woodcut.WoodCutAPI;
import com.botdiril.gamelogic.woodcut.WoodCutInput;
import com.botdiril.userdata.icon.Icons;
import com.botdiril.userdata.items.Items;
import com.botdiril.userdata.tempstat.Curser;
import com.botdiril.userdata.timers.EnumTimer;
import com.botdiril.userdata.timers.TimerUtil;
import com.botdiril.util.BotdirilFmt;
import com.botdiril.util.BotdirilRnd;

@Command("woodcut")
public class CommandWoodCut
{
    private static final String NULL_USER = "\u004e\u0337\u0350\u0349\u031c\u0055\u0335\u030b\u034a\u035d\u0327\u004c\u0335\u030d\u033e\u0314\u0325\u032c\u0349\u034e\u004c\u0336\u0341\u0340\u0360\u0348\u032d\u0349\u034d\u035c\u0020\u0336\u030b\u0302\u0318\u0347\u0055\u0334\u0308\u0313\u0344\u031d\u0053\u0338\u030c\u0305\u0352\u030d\u0358\u0323\u0045\u0337\u035b\u0300\u0358\u034a\u032a\u0052\u0335\u0341\u0359\u035a";

    @CmdInvoke
    public static void woodCut(CommandContext co)
    {
        TimerUtil.require(co.inventory, EnumTimer.WOODCUT, "You need to wait **$** until you can go **woodcutting** again.", false);

        var input = new WoodCutInput(co.inventory.getLevel());

        var result = WoodCutAPI.woodCut(input);

        var timerModifier = 1.0;
        var drops = result.earnedItems();

        switch (result.outcome())
        {
            case OAK_FOREST -> co.respond("You're entering an oak forest. You can hear a river passing nearby.");
            case BIRCH_FOREST -> co.respond("You're entering a beautiful birch forest. You can hear leaves rustle as the mild wind breezes through the treetops.");
            case PINE_FOREST -> co.respond("You're entering a pine forest while a wild fox passes on your right.");
            case SPRUCE_FOREST -> co.respond("You're entering a dark spruce forest. The shade has allowed various mushrooms to grow, however you decide not to try your luck.");
            case ANGRY_LOOKING_TREES -> co.respond("You're entering a dark and twisted forest. One of the trees looks suspiciously angry, however you don't bother it.");
            case AURORA_BOREALIS -> co.respond("You look at the sky, witnessing an amazing show. " +
                                               "Aurora Borealis? At this time of year? At this time of the day? In this part of the country?");
            case GOT_CAUGHT -> co.respond("You get caught tresspassing, being forced to leave early.");
            case MET_MURRAY -> co.respondf("You meet a friendly gnome called Murray. You trade some of your wood for **\"%s a magical scroll\"**.", Icons.SCROLL_RARE);
            case OLD_HOUSE -> co.respondf("You find a semi-demolished old house and after a short investigation you find **%s**.", drops.toStringJoined(", "));
            case NULL_USER -> {
                co.respondf("You meet the legendary %s as he grants you **%s** and then teleports you to an unknown location.", NULL_USER, drops.toStringJoined(", "));
                timerModifier = BotdirilRnd.RDG.nextUniform(2.0, 5.0);
            }
            case FOUND_MAX -> co.respondf("You notice an **%s unexpectly friedly dog** has been following you around. It doesn't have a name tag so you decide to take it with you for the time being.", Icons.OTHER_MAX);
            case CAUSED_WILDFIRE -> co.respondf("Due to recklessness, you cause half of the forest to burn down, leaving only **%s** behind.", drops.toStringJoined(", "));
            case FOUND_KEY -> co.respondf("While working, you notice an **%s old rusted key** laying around.", Icons.KEY);
            case ABANDONED_MINE -> co.respondf("You find an abandoned mine that has unfortunately collapsed. " +
                                           "You grab some tools (**%s**) you find and leave wonderinng whether someone was trapped inside.",
                                                drops.toStringJoined(", "));
            case NEARLY_KILLED_BY_WEREWOLF -> {
                co.respond(":full_moon: You hear howling as you are approaching a small hill. " +
                           "You hide as you realize what you are dealing with and just nearly escape sure death.");
                Curser.curse(co);
            }
            case MET_AZOR -> {
                co.respond(":new_moon: You cannot believe your eyes as you stumble into the local legend - spectral wolf Azor." +
                           " After following it for a while, it wanishes." +
                           " You notice you've taken a shortcut home.");
                timerModifier = 0.5;
            }
            case GOT_LOST -> {
                co.respond("You've wandered too far away and got lost. It will take a while to find a way back.");
                timerModifier = 2.0;
            }
        }

        co.inventory.useTimerModified(EnumTimer.WOODCUT, timerModifier);

        drops.stream().forEach(ip -> co.inventory.addItem(ip.getItem(), ip.getAmount()));

        var earnedWood = result.earnedWood();
        var xp = result.earnedXP();

        if (earnedWood != 0)
            co.inventory.addItem(Items.wood, earnedWood);

        var earnedWoodStr = BotdirilFmt.amountOfMD(earnedWood, Items.wood);
        var xpStr = "**[+%s]**".formatted(BotdirilFmt.amountOf(xp, Icons.XP));

        switch (result.yieldModifier())
        {
            case NOTHING -> co.respondf("You are sadly returning with %s. %s", BotdirilFmt.amountOfMD("no", Items.wood), xpStr);
            case TERRIBLE -> co.respondf("Today isn't the day, you found only %s. %s", earnedWoodStr, xpStr);
            case BAD -> co.respondf("You didn't gather much wood, only %s. %s", earnedWoodStr, xpStr);
            case NORMAL, GOOD -> co.respondf("You collected %s. %s", earnedWoodStr, xpStr);
            case GREAT -> co.respondf("You spent the entire day in the forest and it paid off, earning %s. %s", earnedWoodStr, xpStr);
        }

        co.inventory.addXP(co, xp);
    }
}
