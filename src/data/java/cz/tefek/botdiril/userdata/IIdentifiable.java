package cz.tefek.botdiril.userdata;

public interface IIdentifiable
{
    String getDescription();

    String getIcon();

    int getID();

    String getLocalizedName();

    String getName();

    default boolean hasIcon()
    {
        return this.getIcon() != null;
    }

    String inlineDescription();
}
