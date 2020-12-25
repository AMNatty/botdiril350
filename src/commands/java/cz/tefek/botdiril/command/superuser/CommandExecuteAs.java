package cz.tefek.botdiril.command.superuser;

import net.dv8tion.jda.api.entities.Member;

import cz.tefek.botdiril.framework.command.CallObj;
import cz.tefek.botdiril.framework.command.Command;
import cz.tefek.botdiril.framework.command.CommandCategory;
import cz.tefek.botdiril.framework.command.invoke.CmdInvoke;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.parser.CommandParser;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.userdata.UserInventory;
import cz.tefek.botdiril.userdata.properties.PropertyObject;

@Command(value = "exe", aliases = { "exec", "execute",
        "executeas" }, category = CommandCategory.SUPERUSER, powerLevel = EnumPowerLevel.SUPERUSER_OWNER, description = "Executes a command as someone else.")
public class CommandExecuteAs
{

    @CmdInvoke
    public static void exec(CallObj co, @CmdPar("user") Member member, @CmdPar("command") String command)
    {
        var cobj = new CallObj();
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

        co.respond(String.format("Executing `%s` as `%s`.", command, member.getEffectiveName()));

        CommandParser.parse(cobj);
    }
}
