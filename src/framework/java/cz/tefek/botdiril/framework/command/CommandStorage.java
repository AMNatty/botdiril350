package cz.tefek.botdiril.framework.command;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CommandStorage
{
    private static final Logger logger = LogManager.getLogger(CommandStorage.class);

    private static final Map<String, Command> aliasMap = new HashMap<>();
    private static final Map<Command, Class<?>> classMap = new HashMap<>();

    public static int commandCount()
    {
        return (int) aliasMap.values().stream().distinct().count();
    }

    public static int commandCountInCategory(CommandCategory cat)
    {
        return (int) aliasMap.values().stream().distinct().filter(cmd -> cmd.category().equals(cat)).count();
    }

    public static List<Command> commandsInLevelRange(int previousLevel, int newLevel)
    {
        return classMap.keySet().stream().filter(cmd -> cmd.levelLock() > previousLevel && cmd.levelLock() <= newLevel).collect(Collectors.toList());
    }

    public static Class<?> getAccordingClass(Command key)
    {
        return classMap.get(key);
    }

    public static List<Command> getCommandsByCategory(CommandCategory cat)
    {
        return aliasMap.values().stream().distinct().filter(cmd -> cmd.category().equals(cat)).collect(Collectors.toList());
    }

    static void register(Command command, Class<?> clazz)
    {
        logger.info(command.value() + " of " + clazz);

        aliasMap.put(command.value(), command);

        for (var alias : command.aliases())
        {
            aliasMap.put(alias, command);
        }

        classMap.put(command, clazz);
    }

    public static Command search(String alias)
    {
        return aliasMap.get(alias.toLowerCase());
    }
}
