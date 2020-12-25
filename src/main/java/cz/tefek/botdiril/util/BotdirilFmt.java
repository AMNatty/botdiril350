package cz.tefek.botdiril.util;

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
}
