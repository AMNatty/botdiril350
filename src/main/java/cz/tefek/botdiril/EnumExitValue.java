package cz.tefek.botdiril;

/**
 * Enum containing possible exit codes of the application.
 */
public enum EnumExitValue
{
    /**
     * The application exited with no issues.
     */
    OK(0),
    /**
     * The application was terminated during initialization.
     */
    EXCEPTION_DURING_INITIALIZATION(0x01);

    private int code;

    EnumExitValue(int code)
    {
        this.code = code;
    }

    /**
     * Gets the code of this {@link EnumExitValue}.
     */
    public int getCode()
    {
        return this.code;
    }
}
