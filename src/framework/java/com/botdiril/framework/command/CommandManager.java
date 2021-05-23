package com.botdiril.framework.command;

import com.botdiril.BotMain;
import com.botdiril.util.BotdirilLog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CommandManager
{
    private static final Logger logger = LogManager.getLogger(CommandManager.class);

    private static final EnumMap<EnumCommandCategory, Set<Command>> categoryMap = new EnumMap<>(EnumCommandCategory.class);
    private static final Map<Command, CommandInfo> commandInfoMap = new HashMap<>();
    private static final Map<String, Command> aliasMap = new HashMap<>();
    private static final Map<Command, Class<?>> classMap = new HashMap<>();


    public static Stream<Command> commands()
    {
        return classMap.keySet().stream();
    }

    public static int commandCount()
    {
        return commandInfoMap.size();
    }

    public static int commandCountInCategory(EnumCommandCategory cat)
    {
        return categoryMap.get(cat).size();
    }

    public static List<Command> commandsInLevelRange(int previousLevel, int newLevel)
    {
        return commandInfoMap.entrySet().stream().filter(cmd -> {
            var cmdInfo = cmd.getValue();
            return cmdInfo.levelLock() > previousLevel && cmdInfo.levelLock() <= newLevel;
        }).map(Map.Entry::getKey).collect(Collectors.toList());
    }

    public static Class<?> getAccordingClass(Command key)
    {
        return classMap.get(key);
    }

    public static CommandInfo getCommandInfo(Command key)
    {
        return commandInfoMap.get(key);
    }

    public static Set<Command> getCommandsByCategory(EnumCommandCategory cat)
    {
        return categoryMap.get(cat);
    }

    public static Command findCommand(String alias)
    {
        return aliasMap.get(alias.toLowerCase());
    }

    public static void load()
    {
        var categories = EnumCommandCategory.values();
        var commandNameToCategoryMap = new HashMap<String, EnumCommandCategory>();
        var commandNameToInfoMap = new HashMap<String, CommandInfo>();

        Arrays.stream(categories).forEach(category -> {
            var info = category.getInfo();
            var commands = info.commands();

            commands.forEach((command, commandInfo) -> {
                commandNameToCategoryMap.put(command, category);
                commandNameToInfoMap.put(command, commandInfo);
            });

            categoryMap.put(category, new HashSet<>());
        });


        var commands = CommandCompiler.load();

        commands.forEach((command, clazz) -> {
            var commandName = command.value();

            logger.info("%s of '%s'".formatted(commandName, clazz));

            var info = commandNameToInfoMap.get(commandName);

            if (info == null)
            {
                logger.error("Command '%s' info not found!".formatted(commandName));
                return;
            }

            var category = commandNameToCategoryMap.get(commandName);

            commandInfoMap.put(command, info);
            categoryMap.get(category).add(command);
            classMap.put(command, clazz);

            aliasMap.put(command.value(), command);
            for (var alias : info.aliases())
                aliasMap.put(alias, command);

        });
    }

    public static void unload()
    {
        unload(() -> BotdirilLog.logger.info("Unloading complete..."));
    }

    public static void unload(Runnable andThen)
    {
        var exec = Executors.newSingleThreadExecutor();
        exec.submit(() -> {
            var eventBus = BotMain.botdiril.getEventBus();
            var writeLock = eventBus.ACCEPTING_COMMANDS.writeLock();

            try
            {
                writeLock.lock();

                categoryMap.clear();
                commandInfoMap.clear();
                aliasMap.clear();
                classMap.clear();

                CommandCompiler.unload();
            }
            finally
            {
                writeLock.unlock();
                andThen.run();
            }
        });
    }
}
