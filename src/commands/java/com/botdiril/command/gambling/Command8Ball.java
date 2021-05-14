package com.botdiril.command.gambling;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;
import com.botdiril.util.BotdirilRnd;

@Command(value = "8ball", category = CommandCategory.GAMBLING, description = "Ask the 8ball.")
public class Command8Ball
{
    private static final String[] answers = { "It is certain.", "It is decidedly so.", "Without a doubt.",
            "Yes - definitely.", "You may rely on it.", "As I see it, yes.", "Most likely.", "Outlook good.", "Yes.",
            "Signs point to yes.", "Reply hazy, try again", "Ask again later.", "Better not tell you now.",
            "Cannot predict now.", "Concentrate and ask again.", "Don't count on it.", "My reply is no.",
            "My sources say no.", "Outlook not so good.", "Very doubtful." };

    @CmdInvoke
    public static void roll(CommandContext co, @CmdPar("question") String question)
    {
        CommandAssert.stringNotTooLong(question, 300, "Your question is too long, please make it shorter.");

        var eb = new ResponseEmbed();
        eb.setAuthor(co.player.getName() + "'s question");
        eb.setDescription(question);
        eb.setColor(0x008080);
        eb.addField("Answer:", ":8ball: | " + BotdirilRnd.choose(answers), false);

        co.respond(eb);
    }
}
