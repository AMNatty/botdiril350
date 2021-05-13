package com.botdiril.command.superuser;

import com.botdiril.framework.command.Command;
import com.botdiril.framework.command.CommandCategory;
import com.botdiril.framework.command.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;
import com.botdiril.framework.command.parser.CommandParser;
import com.botdiril.framework.permission.EnumPowerLevel;
import com.botdiril.userdata.properties.PropertyObject;
import net.dv8tion.jda.api.entities.Member;

import com.botdiril.userdata.UserInventory;

@Command(value = "exe", aliases = { "exec", "execute",
        "executeas" }, category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OWNER, description = "Executes a command as someone else.")
public class CommandExecuteAs
{

    @CmdInvoke
    public static void exec(CommandContext co, @CmdPar("user") Member member, @CmdPar("command") String command)
    {
        var cobj = new CommandContext();
        cobj.caller = member.getUser();
        cobj.ui = new UserInventory(co.db, cobj.caller.getIdLong());
        cobj.db = co.db;
        cobj.po = new PropertyObject(co.db, cobj.ui.getFID());
        cobj.callerMember = member;
        cobj.message = co.message;
        cobj.contents = command;
        cobj.jda = co.jda;
        cobj.sc = co.sc;
        cobj.guild = co.guild;
        cobj.textChannel = co.textChannel;
        cobj.bot = co.bot;
        cobj.usedAlias = co.usedAlias;
        cobj.usedPrefix = co.usedPrefix;

        co.respond(String.format("Executing `%s` as `%s`.", command, member.getEffectiveName()));

        CommandParser.parse(cobj);
    }
}
