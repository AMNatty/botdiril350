package com.botdiril.command.superuser;

import com.botdiril.discord.framework.DiscordEntityPlayer;
import com.botdiril.discord.framework.command.context.DiscordCommandContext;
import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.parser.CommandParser;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.userdata.properties.PropertyObject;
import net.dv8tion.jda.api.entities.Member;

@Command(value = "exe", aliases = { "exec", "execute",
        "executeas" }, category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OWNER, description = "Executes a command as someone else.")
public class CommandExecuteAs
{

    @CmdInvoke
    public static void exec(DiscordCommandContext co, @CmdPar("user") Member member, @CmdPar("command") String command)
    {
        var cobj = new DiscordCommandContext(co.textChannel);
        cobj.botPlayer = new DiscordEntityPlayer(co.db, co.bot);
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

        co.respondf("Executing `%s` as `%s`.", command, member.getEffectiveName());
        co.send();

        CommandParser.parse(cobj);
    }
}
