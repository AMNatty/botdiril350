package com.botdiril.framework.command;

import com.botdiril.MajorFailureException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarFile;

public class CommandIntitializer
{
    private static final Logger logger = LogManager.getLogger(CommandIntitializer.class);

    static Map<Command, Class<?>> load()
    {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info("Classloading begin: ");

        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

        var foundCommands = new HashMap<Command, Class<?>>();

        for (var cp : classpathEntries)
        {
            try
            {
                var cpFile = new File(cp);

                if (cpFile.isDirectory())
                {
                    var files = FileUtils.listFiles(cpFile, null, true);

                    URL[] urls = { cpFile.toURI().toURL() };
                    var sysLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());

                    for (var classFile : files)
                    {
                        if (!classFile.getName().endsWith(".class"))
                            continue;

                        var className = cpFile.toURI().relativize(classFile.toURI()).getPath().replaceAll("\\.class$", "").replace('/', '.');

                        var modClass = sysLoader.loadClass(className);
                        var annotation = modClass.getDeclaredAnnotation(Command.class);

                        if (annotation != null)
                        {
                            foundCommands.put(annotation, modClass);
                        }
                    }
                }
                else if (cpFile.getName().endsWith(".jar"))
                {
                    var jarFile = new JarFile(cp);
                    var e = jarFile.entries();

                    URL[] urls = { new URL("jar:file:" + cp + "!/") };

                    var sysLoader = URLClassLoader.newInstance(urls, ClassLoader.getSystemClassLoader());

                    while (e.hasMoreElements())
                    {
                        var je = e.nextElement();

                        if (je.isDirectory())
                            continue;

                        var jeName = je.getName();

                        if (!jeName.startsWith("com/botdiril"))
                            continue;

                        if (!jeName.endsWith(".class") || jeName.endsWith("module-info.class"))
                            continue;

                        var className = jeName.replaceAll("\\.class$", "").replace('/', '.');

                        var modClass = sysLoader.loadClass(className);
                        var annotation = modClass.getDeclaredAnnotation(Command.class);

                        if (annotation != null)
                        {
                            foundCommands.put(annotation, modClass);
                        }
                    }

                    jarFile.close();
                }
            }
            catch (Exception e)
            {
                throw new MajorFailureException("An exception has occurred while initializing commands.", e);
            }
        }

        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");

        return foundCommands;
    }
}
