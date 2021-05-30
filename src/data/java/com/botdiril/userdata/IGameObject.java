package com.botdiril.userdata;

public interface IGameObject extends ILocalized, INamedObject
{
    String getInlineDescription();

    String getDescription();

    String getIcon();

    default boolean hasIcon()
    {
        return this.getIcon() != null;
    }
}
