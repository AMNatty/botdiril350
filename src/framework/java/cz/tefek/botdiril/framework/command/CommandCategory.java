package cz.tefek.botdiril.framework.command;

public enum CommandCategory
{
    GENERAL("General commands"),
    CURRENCY("Currency commands"),
    ITEMS("Inventory commands"),
    GAMBLING("Gambling commands"),
    ADMINISTRATIVE("Administrative commands"),
    INTERACTIVE("Interactive commands"),
    SUPERUSER("Superuser commands");

    private final String name;

    CommandCategory(String humanReadableName)
    {
        this.name = humanReadableName;
    }

    public String getName()
    {
        return name;
    }
}
