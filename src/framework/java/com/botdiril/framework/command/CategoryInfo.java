package com.botdiril.framework.command;

import java.util.Map;

public record CategoryInfo(
    String name,
    Map<String, CommandInfo> commands
)
{
}
