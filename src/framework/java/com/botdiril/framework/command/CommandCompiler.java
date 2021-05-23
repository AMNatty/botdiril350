package com.botdiril.framework.command;

import com.botdiril.MajorFailureException;
import com.botdiril.util.BotdirilLog;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Function;

public class CommandCompiler
{
    private static CommandClassLoader classLoader;
    private static final Path COMMANDS_DIR = Path.of("assets", "commands");
    private static final Map<Command, Class<?>> foundCommands = new HashMap<>();
    private static final Map<String, Path> foundClasses = new HashMap<>();

    static Map<Command, Class<?>> load()
    {
        try
        {
            classLoader = new CommandClassLoader();

            try (var files = Files.list(COMMANDS_DIR))
            {
                files.forEach(CommandCompiler::tryLoadCommandGroup);
            }

            foundClasses.keySet().forEach(CommandCompiler::compile);

            var classes = classLoader.getClasses();

            classes.forEach(CommandCompiler::tryLoadCommands);

            return foundCommands;
        }
        catch (Exception e)
        {
            throw new MajorFailureException("Failed to load command classes:", e);
        }
    }

    static void unload()
    {
        BotdirilLog.logger.info("Unloading command classes and the classloader.");

        classLoader = null;
        foundClasses.clear();
        foundCommands.clear();

        System.gc();
    }

    private static void tryLoadCommands(Class<?> clazz)
    {
        var annotation = clazz.getDeclaredAnnotation(Command.class);

        if (annotation != null)
        {
            foundCommands.put(annotation, clazz);
        }
    }

    private static void tryLoadCommandGroup(Path path)
    {
        try
        {
            if (!Files.isDirectory(path))
                return;

            var javaCommandsDir = path.resolve("java");
            if (!Files.isDirectory(javaCommandsDir))
                return;

            try (var tree = Files.walk(javaCommandsDir))
            {
                tree.filter(Files::isRegularFile)
                    .forEach(CommandCompiler::tryLoadFile);
            }
        }
        catch (Exception e)
        {
            throw new MajorFailureException("Failed to load command group:", e);
        }
    }

    private static void tryLoadFile(Path file)
    {
        var fileNameStr = file.toString();

        try
        {
            if (!fileNameStr.endsWith(".java"))
                return;

            var relativeFile = COMMANDS_DIR.relativize(file);
            var sourcePath = relativeFile.subpath(2, relativeFile.getNameCount());
            var fs = FileSystems.getDefault();

            var sourcePathStr = sourcePath.toString();

            var className = sourcePathStr
                .replaceAll("\\.java$", "")
                .replace(fs.getSeparator(), ".");

            BotdirilLog.logger.info("Compiling class '{}' from '{}'", className, fileNameStr);

            foundClasses.put(className, file);
        }
        catch (Exception e)
        {
            throw new MajorFailureException("Failed to load command class %s:".formatted(fileNameStr), e);
        }
    }

    private static void compile(String className)
    {
        try
        {
            classLoader.loadClass(className);
        }
        catch (ClassNotFoundException clazzNotFoundExcept)
        {
            try
            {
                Function<Map.Entry<String, Path>, StringJavaFileObject> loader = entry -> loadObject(entry.getKey(), entry.getValue());
                compileFiles(foundClasses.entrySet().stream().map(loader).toList());
            }
            catch (ClassNotFoundException e)
            {
                throw new RuntimeException(e);
            }
        }
    }

    private static StringJavaFileObject loadObject(String className, Path path)
    {
        try
        {
            return new StringJavaFileObject(className, Files.readString(path));
        }
        catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private static void compileFiles(List<StringJavaFileObject> files) throws ClassNotFoundException
    {
        JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();

        var standardFileManager = compiler.getStandardFileManager(null, null, StandardCharsets.UTF_8);
        ClassFileManager fileManager = new ClassFileManager(standardFileManager);

        var compileTask = compiler.getTask(null, fileManager, null, null, null, files);
        compileTask.call();

        if (fileManager.objects.contains(null))
            throw new RuntimeException("Compile failed.");

        fileManager.objects.forEach(object -> classLoader.createClass(object.getClassName(), object.getBytes()));
    }

    private static final class JavaFileObject extends SimpleJavaFileObject
    {
        private final String className;
        private final ByteArrayOutputStream baos;

        JavaFileObject(String name, JavaFileObject.Kind kind)
        {
            super(URI.create("string:///" + name.replace('.', '/') + kind.extension), kind);
            this.baos = new ByteArrayOutputStream();
            this.className = name;
        }

        public String getClassName()
        {
            return this.className;
        }

        byte[] getBytes()
        {
            return baos.toByteArray();
        }

        @Override
        public OutputStream openOutputStream()
        {
            return baos;
        }
    }

    private static class ClassFileManager extends ForwardingJavaFileManager<StandardJavaFileManager>
    {
        private final List<JavaFileObject> objects;

        ClassFileManager(StandardJavaFileManager fileManager)
        {
            super(fileManager);
            this.objects = new ArrayList<>();
        }

        @Override
        public JavaFileObject getJavaFileForOutput(JavaFileManager.Location location, String className, JavaFileObject.Kind kind, FileObject sibling)
        {
            var object = new JavaFileObject(className, kind);
            objects.add(object);
            return object;
        }
    }

    private static class StringJavaFileObject extends SimpleJavaFileObject
    {
        private final String content;

        public StringJavaFileObject(String className, String content)
        {
            super(URI.create("string:///" + className.replace('.', '/') + JavaFileObject.Kind.SOURCE.extension), JavaFileObject.Kind.SOURCE);
            this.content = content;
        }

        @Override
        public String getCharContent(boolean ignoreEncodingErrors)
        {
            return this.content;
        }
    }

    public static class CommandClassLoader extends ClassLoader
    {
        private final Map<String, Class<?>> classMap;
        private final Map<String, URL> resourceMap;

        private CommandClassLoader()
        {
            this.classMap = new HashMap<>();
            this.resourceMap = new HashMap<>();
        }

        @Override
        protected Class<?> findClass(String name) throws ClassNotFoundException
        {
            var clazz = this.classMap.get(name);

            if (clazz == null)
            {
                throw new ClassNotFoundException(String.format("Undefined class: '%s'", name));
            }

            return clazz;
        }

        public Collection<Class<?>> getClasses()
        {
            return Collections.unmodifiableCollection(classMap.values());
        }

        @Override
        protected URL findResource(String name)
        {
            return this.resourceMap.get(name);
        }

        @Override
        protected Enumeration<URL> findResources(String name)
        {
            var resource = this.findResource(name);
            return resource == null ? Collections.emptyEnumeration() : Collections.enumeration(List.of(resource));
        }

        private void createClass(String name, byte[] data)
        {
            var clazz = this.defineClass(name, data, 0, data.length);
            this.resolveClass(clazz);
            this.classMap.put(name, clazz);
        }

        private void createResource(String name, URL url)
        {
            this.resourceMap.put(name, url);
        }
    }
}
