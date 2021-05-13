package com.botdiril;

public class MajorFailureException extends RuntimeException
{
    public MajorFailureException(String message, Exception cause)
    {
        super(message, cause);
    }

    public MajorFailureException(String message)
    {
        super(message);
    }
}
