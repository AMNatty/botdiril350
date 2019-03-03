package cz.tefek.botdiril;

import java.util.Locale;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class BotMain
{
    /**
     * Singleton global Logger instance.
     */
    public static Logger logger;

    /**
     * Singleton Botdiril instance.
     */
    public static Botdiril bot;

    /**
     * The main function, creating an instance of Botdiril and ensuring
     * configuration loading.
     */
    public static void main(String[] args)
    {
        logger = Logger.getLogger("Botdiril Global Log");
        logger.setLevel(Level.DEBUG);

        logger.info("=====================================");
        logger.info("#           BOTDIRIL400             #");
        logger.info("=====================================");

        Locale.setDefault(Locale.US);

        // Try to run the bot, terminate on failure.
        try
        {
            bot = new Botdiril();
            bot.start();
        }
        catch (Exception e)
        {
            logger.fatal("A fatal error has occured while loading Botdiril. :(", e);
            logger.fatal("The system will now exit.");

            LogManager.shutdown();
            System.exit(EnumExitValue.EXCEPTION_DURING_INITIALIZATION.getCode());
        }
    }
}
