package com.botdiril.framework.command;

import com.botdiril.framework.command.context.CommandContext;
import com.botdiril.framework.command.invoke.CmdInvoke;
import com.botdiril.framework.command.invoke.CmdPar;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class CommandIntrospector
{
    public static List<Method> listMethods(Command command)
    {
        var cmdClass = CommandStorage.getAccordingClass(command);

        var methods = cmdClass.getDeclaredMethods();

        return Arrays.stream(methods).filter(meth ->
        {
            var hasInvoke = meth.getDeclaredAnnotation(CmdInvoke.class) != null;

            if (!hasInvoke)
            {
                return false;
            }

            if (meth.getParameterCount() < 1)
            {
                return false;
            }

            var pars = meth.getParameters();

            var first = pars[0].getType();

            if (!CommandContext.class.isAssignableFrom(first))
            {
                return false;
            }

            int i = 0;

            for (var parameter : pars)
            {
                if (i > 0 && parameter.getDeclaredAnnotation(CmdPar.class) == null)
                {
                    return false;
                }
                i++;
            }

            return true;
        }).sorted(Comparator.comparing(Method::getParameterCount).reversed()).collect(Collectors.toList());
    }
}
