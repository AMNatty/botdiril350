package cz.tefek.botdiril.framework.command.parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.*;
import cz.tefek.botdiril.framework.command.invoke.CmdPar;
import cz.tefek.botdiril.framework.command.invoke.CommandException;
import cz.tefek.botdiril.framework.permission.EnumPowerLevel;
import cz.tefek.botdiril.framework.sql.DBException;
import cz.tefek.botdiril.serverdata.ChannelPreferences;
import cz.tefek.botdiril.userdata.stat.EnumStat;
import cz.tefek.botdiril.util.BotdirilLog;

public class CommandParser
{
    public static boolean parse(CallObj co)
    {
        var cmdParts =  co.contents.split("\\s+", 2);
        var cmdStr = cmdParts[0];
        var cmdParams =  cmdParts.length == 2 ? cmdParts[1] : "";

        var command = CommandStorage.search(cmdStr);

        if (command == null)
        {
            return true;
        }

        co.usedAlias = cmdStr;

        var special = Arrays.asList(command.special());

        if (!special.contains(EnumSpecialCommandProperty.ALLOW_LOCK_BYPASS))
        {
            if (ChannelPreferences.checkBit(co.db, co.textChannel.getIdLong(), ChannelPreferences.BIT_DISABLED) && !EnumPowerLevel.ELEVATED.check(co.db, co.callerMember, co.textChannel))
            {
                return true;
            }
        }

        if (!command.powerLevel().check(co.db, co.callerMember, co.textChannel))
        {
            co.respond(String.format("You need to have the **%s** power level to use this command!", command.powerLevel().toString()));

            return true;
        }

        if (co.ui.getLevel() < command.levelLock() && !(EnumPowerLevel.SUPERUSER_OVERRIDE.check(co.db, co.callerMember, co.textChannel) || EnumPowerLevel.VIP.check(co.db, co.callerMember, co.textChannel)))
        {
            co.respond(String.format("You need at least level **%d** to do this.", command.levelLock()));

            return true;
        }

        var commandFunc = CommandIntrospector.listMethods(command);

        for (var meth : commandFunc)
        {
            var parameters = Arrays.stream(meth.getParameters())
                .filter(param -> param.getDeclaredAnnotation(CmdPar.class) != null)
                .collect(Collectors.toList());

            var args = ArgParser.splitArgs(parameters, cmdParams);

            if (args == null)
                continue;

            try
            {
                var argArr = new Object[parameters.size() + 1];
                argArr[0] = co;

                for (int i = 1; i < argArr.length; i++)
                {
                    var parameter = parameters.get(i - 1);
                    var clazz = parameter.getType();
                    var arg = args.get(i - 1);
                    var ant = parameter.getAnnotation(CmdPar.class);
                    var type = ant.type();

                    argArr[i] = CommandParserTypeHandler.handleType(co, clazz, argArr, type, i, arg);
                }

                try
                {
                    co.po.incrementStat(EnumStat.COMMANDS_USED);
                    meth.invoke(null, argArr);
                    return true;
                }
                catch (IllegalAccessException | IllegalArgumentException e)
                {
                    if (e instanceof IllegalArgumentException)
                    {
                        BotdirilLog.logger.fatal("Argument type mismatch: " + Arrays.stream(argArr).map(Object::getClass).map(Class::toString).collect(Collectors.toList()));
                        BotdirilLog.logger.fatal("Expected: " + parameters.stream().map(Parameter::getType).map(Class::toString).collect(Collectors.toList()));
                    }

                    co.db.rollback();
                    co.respond("**An error has occured while processing the command.**\nPlease report this to the bot owner.");
                    BotdirilLog.logger.fatal("An exception has occured while invoking a command.", e);
                    return false;
                }
                catch (InvocationTargetException e)
                {
                    if (e.getCause() instanceof CommandException)
                    {
                        co.db.rollback();
                        co.respond(e.getCause().getMessage());
                        return false;
                    }
                    else
                    {
                        co.db.rollback();
                        co.respond("**An error has occured while processing the command.**\nPlease report this to the bot owner.");
                        BotdirilLog.logger.fatal("An exception has occured while invoking a command.", e.getCause());
                        return false;
                    }
                }
            }
            catch (CommandException e)
            {
                co.db.rollback();

                if (e.isEmbedded())
                    co.respond(e.getEmbed());
                else
                    co.respond(e.getMessage());

                return false;
            }
            catch (DBException e)
            {
                co.db.rollback();
                co.respond("**An error has occured while processing the command.**\nPlease report this to the bot owner.");
                BotdirilLog.logger.fatal("A database error has occured while invoking a command.", e);
                return false;
            }
            catch (Exception e)
            {
                co.db.rollback();
                co.respond("**An error has occured while processing the command.**\nPlease report this to the bot owner.");
                BotdirilLog.logger.fatal("An exception has occured while invoking a command.", e);
                return false;
            }
        }

        String error = "Error! Wrong arguments.\n**Usage:**\n" + GenUsage.usage(co.usedPrefix, co.usedAlias, command);
        co.respond(error);
        return true;
    }

}
