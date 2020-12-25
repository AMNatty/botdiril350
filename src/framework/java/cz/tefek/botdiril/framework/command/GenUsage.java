package cz.tefek.botdiril.framework.command;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

import cz.tefek.botdiril.framework.command.invoke.CmdPar;

public class GenUsage
{
    public static String usage(String prefix, String alias, Command cmd)
    {
        var sb = new StringBuilder();

        var commandFunc = CommandIntrospector.listMethods(cmd);

        for (var meth : commandFunc)
        {
            var parameters = Arrays.stream(meth.getParameters())
                .map(param -> param.getDeclaredAnnotation(CmdPar.class))
                .filter(Objects::nonNull)
                .map(par -> "<" + par.value() + ">")
                .collect(Collectors.joining(" "));

            sb.append('`');
            sb.append(prefix);
            sb.append(alias);
            if (!parameters.isEmpty())
                sb.append(' ');
            sb.append(parameters);
            sb.append('`');
            sb.append("\n");
        }

        return sb.toString();
    }
}
