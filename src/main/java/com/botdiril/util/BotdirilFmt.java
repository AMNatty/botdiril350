package com.botdiril.util;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class BotdirilFmt
{
    private static final DecimalFormat doubleFormat;
    private static final DecimalFormat longFormat;

    static
    {
        var formatSymbols = new DecimalFormatSymbols(Locale.getDefault());
        formatSymbols.setDecimalSeparator('.');
        formatSymbols.setGroupingSeparator('\'');

        var doubleFormatStr = "#,##0.##";
        doubleFormat = new DecimalFormat(doubleFormatStr, formatSymbols);
        doubleFormat.setGroupingSize(3);

        var longFormatStr = "#,##0";
        longFormat = new DecimalFormat(longFormatStr, formatSymbols);
        longFormat.setGroupingSize(3);
    }

    public static String format(double number)
    {
        return doubleFormat.format(number);
    }

    public static String format(long number)
    {
        return longFormat.format(number);
    }

    public static String amountOf(double number, Object item)
    {
        return "%s\u00A0%s".formatted(doubleFormat.format(number), item);
    }

    public static String amountOf(long number, Object item)
    {
        return "%s\u00A0%s".formatted(longFormat.format(number), item);
    }

    public static String amountOf(String number, Object item)
    {
        return "%s\u00A0%s".formatted(number, item);
    }

    public static String amountOfMD(double number, Object item)
    {
        return "**%s\u00A0%s**".formatted(doubleFormat.format(number), item);
    }

    public static String amountOfMD(long number, Object item)
    {
        return "**%s\u00A0%s**".formatted(longFormat.format(number), item);
    }

    public static String amountOfMD(String number, Object item)
    {
        return "**%s\u00A0%s**".formatted(number, item);
    }
}
