package com.botdiril.command;

import com.botdiril.discord.framework.DiscordEntityPlayer;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.parser.CommandParser;
import com.botdiril.userdata.properties.PropertyObject;
import net.dv8tion.jda.api.entities.Member;

@Command("execute")
public class CommandExecuteAs
{
    @CmdInvoke
    public static void exec(DiscordCommandContext co, @CmdPar("user") Member member, @CmdPar("command") String command)
    {
        var cobj = new DiscordCommandContext(co.textChannel);
        cobj.botPlayer = co.botPlayer;
        cobj.botIconURL = co.botIconURL;
        cobj.caller = member.getUser();
        cobj.player = new DiscordEntityPlayer(co.db, member);
        cobj.inventory = cobj.player.inventory();
        cobj.db = co.db;
        cobj.userProperties = new PropertyObject(co.db, cobj.inventory.getFID());
        cobj.callerMember = member;
        cobj.message = co.message;
        cobj.contents = command;
        cobj.jda = co.jda;
        cobj.sc = co.sc;
        cobj.guild = co.guild;
        cobj.bot = co.bot;
        cobj.usedAlias = co.usedAlias;
        cobj.usedPrefix = co.usedPrefix;
        cobj.random = co.random;
        cobj.rdg = co.rdg;

        co.respondf("Executing `%s` as `%s`.", command, member.getEffectiveName());
        co.send();

        CommandParser.parse(cobj);
        cobj.send();
    }
}
