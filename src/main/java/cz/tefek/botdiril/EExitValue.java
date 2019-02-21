package cz.tefek.botdiril;

/**
 * Enum containing possible exit codes of the application.
 */
public enum EExitValue
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

    EExitValue(int code)
    {
        this.code = code;
    }

    /**
     * Gets the code of this {@link EExitValue}.
     */
    public int getCode()
    {
        return this.code;
    }
}
