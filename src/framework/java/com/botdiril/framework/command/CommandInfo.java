package com.botdiril.framework.command;

import com.botdiril.framework.permission.EnumPowerLevel;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.util.StdConverter;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;

@JsonDeserialize(converter = CommandInfo.CommandInfoConverter.class)
public record CommandInfo(
    Set<String> aliases,
    EnumPowerLevel powerLevel,
    int levelLock,
    String description,
    EnumSet<EnumSpecialCommandProperty> special
)
{
    /**
     * This is so tragic.
     *
     * Recreate the {@link CommandInfo} object with the correct default parameters.
     * */
    static class CommandInfoConverter extends StdConverter<CommandInfo, CommandInfo>
    {
        @Override
        public CommandInfo convert(CommandInfo  info)
        {
            return new CommandInfo(
                Objects.requireNonNullElseGet(info.aliases(), Set::of),
                Objects.requireNonNullElse(info.powerLevel(), EnumPowerLevel.EVERYONE),
                info.levelLock(),
                Objects.requireNonNull(info.description(), "<description missing>"),
                EnumSet.noneOf(EnumSpecialCommandProperty.class)
            );
        }
    }
}
