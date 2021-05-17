package com.botdiril.command.general;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandManager;
import com.botdiril.framework.command.EnumCommandCategory;
import com.botdiril.framework.command.GenUsage;
import com.botdiril.framework.command.context.ChatCommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.invoke.CommandException;
import com.botdiril.framework.response.ResponseEmbed;
import com.botdiril.framework.util.CommandAssert;

import java.awt.*;
import java.util.Arrays;
import java.util.stream.Collectors;

@Command("help")
public class CommandHelp
{
    @CmdInvoke
    public static void show(ChatCommandContext co)
    {
        var eb = new ResponseEmbed();
        eb.setColor(Color.CYAN.getRGB());
        eb.setTitle("Stuck? Here is your help:");

        Arrays.stream(EnumCommandCategory.values()).forEach(cat -> {
            var info = cat.getInfo();
            eb.addField(
                "%s [%d]".formatted(info.name(), CommandManager.commandCountInCategory(cat)),
                "Type `%s%s %s`".formatted( co.usedPrefix, co.usedAlias, cat.toString().toLowerCase()),
                false);
        });

        long cmdCnt = CommandManager.commandCount();
        int catCnt = EnumCommandCategory.values().length;

        eb.setDescription("There are %d commands in %d categories total.".formatted(cmdCnt, catCnt));

        co.respond(eb);
    }

    @CmdInvoke
    public static void show(ChatCommandContext co, @CmdPar("category or command") String tbp)
    {
        try
        {
            var command = CommandAssert.parseCommand(tbp);
            var sb = new StringBuilder("**Command `%s`**:".formatted(command.value()));
            var info = CommandManager.getCommandInfo(command);
            var aliases = info.aliases();

            if (!aliases.isEmpty())
            {
                sb.append("\n**Aliases:** ");
                sb.append(aliases.stream().map("`%s`"::formatted).collect(Collectors.joining(", ")));
            }

            sb.append("\n**Description:** ");
            sb.append(info.description());
            sb.append("\n**Power level required:** ");
            sb.append(info.powerLevel());
            sb.append("\n**Available from level:** ");
            sb.append(info.levelLock() == 0 ? "Always available" : info.levelLock());
            sb.append("\n**Usage:**\n");
            sb.append(GenUsage.usage(co.usedPrefix, tbp, command));

            co.respond(sb.toString());
        }
        catch (CommandException e)
        {
            var found = CommandAssert.parseCommandGroup(tbp);

            var eb = new ResponseEmbed();
            eb.setColor(Color.CYAN.getRGB());
            eb.setTitle("Help for the %s category".formatted(found.getInfo().name()));

            CommandManager.getCommandsByCategory(found).forEach(comm -> eb.addField(comm.value(), CommandManager.getCommandInfo(comm).description(), false));

            eb.setDescription("Type `%s%s <command>` to show more information for each command.".formatted(co.usedPrefix, co.usedAlias));

            co.respond(eb);
        }
    }
}
