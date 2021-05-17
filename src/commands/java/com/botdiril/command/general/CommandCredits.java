package com.botdiril.command.general;

import com.botdiril.Botdiril;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.response.ResponseEmbed;

@Command("credits")
public class CommandCredits
{
    @CmdInvoke
    public static void displayCredits(CommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setAuthor(Botdiril.BRANDING, Botdiril.REPO_URL, co.botIconURL);
        eb.setColor(0xFF00000);
        eb.setTitle("Credits");
        eb.addField("Tefek", "Creator and lead developer", false);
        eb.addField("Fand", "Gameplay design, playtesting and funding", false);
        eb.addField("TCM Murray", "Gameplay design & advisory", false);
        eb.addField("Svenmoon", "Gameplay advisory & early testing", false);
        eb.addField("D1firehail", "Gameplay advisory", false);
        eb.addField("", """
        **Special thanks also goes to:**
        Catloaf, Couchi, Weve, Karasma, Thomas, Stronge, Sharkdundo for early playtesting and feedback;
        and YOU, the player. <3
        """, false);
        eb.setThumbnail(co.botIconURL);

        co.respond(eb);
    }
}
