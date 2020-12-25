package cz.tefek.botdiril.framework.command;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

import cz.tefek.botdiril.MajorFailureException;

public class CommandIntitializer
{
    private static final Logger logger = LogManager.getLogger(CommandIntitializer.class);

    public static void load()
    {
        logger.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
        logger.info("Classloading begin: ");

        String classpath = System.getProperty("java.class.path");
        String[] classpathEntries = classpath.split(File.pathSeparator);

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
                            CommandStorage.register(annotation, modClass);
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

                        if (!jeName.startsWith("cz/tefek/botdiril"))
                            continue;

                        if (!jeName.endsWith(".class") || jeName.endsWith("module-info.class"))
                            continue;

                        var className = jeName.replaceAll("\\.class$", "").replace('/', '.');

                        var modClass = sysLoader.loadClass(className);
                        var annotation = modClass.getDeclaredAnnotation(Command.class);

                        if (annotation != null)
                        {
                            CommandStorage.register(annotation, modClass);
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
    }
}
