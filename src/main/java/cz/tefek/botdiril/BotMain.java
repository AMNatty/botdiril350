package cz.tefek.botdiril;

import java.util.Locale;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class BotMain
{
    public static Logger logger;
    public static Botdiril bot;

    public static void main(String[] args)
    {
        logger = Logger.getLogger("Botdiril Global Log");
        logger.setLevel(Level.DEBUG);

        logger.info("=====================================");
        logger.info("#           BOTDIRIL400             #");
        logger.info("=====================================");

        Locale.setDefault(Locale.US);

        try
        {
            bot = new Botdiril();
            bot.start();
        }
        catch (Exception e)
        {
            logger.fatal("A fatal error has occured while loading Botdiril. :(", e);
        }
    }
}
