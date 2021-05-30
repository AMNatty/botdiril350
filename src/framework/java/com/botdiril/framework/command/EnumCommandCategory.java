package com.botdiril.framework.command;

import com.botdiril.MajorFailureException;
import com.botdiril.util.BotdirilLog;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Locale;

public enum EnumCommandCategory
{
    ADMINISTRATIVE,
    COMPETITIVE,
    ECONOMY,
    FUN,
    GAMBLING,
    GENERAL,
    INCOME,
    ITEMS,
    SUPERUSER;

    private final CategoryInfo info;

    EnumCommandCategory()
    {
        var name = this.name();

        BotdirilLog.logger.info("Loading category info '{}'", name);

        var lowerName = name.toLowerCase(Locale.ROOT);

        try (var reader = Files.newBufferedReader(Path.of("assets", "commands", lowerName, "group-info.yaml")))
        {
            var mapper = new ObjectMapper(new YAMLFactory());
            this.info = mapper.readValue(reader, CategoryInfo.class);
        }
        catch (Exception e)
        {
            throw new MajorFailureException("Failed to initialize command groups:", e);
        }
    }

    public CategoryInfo getInfo()
    {
        return this.info;
    }
}
