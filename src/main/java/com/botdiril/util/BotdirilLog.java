package com.botdiril.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BotdirilLog
{
    public static Logger logger;

    public static void init()
    {
        logger = LogManager.getLogger("Botdiril Global Log");
        logger.atLevel(Level.DEBUG);
    }
}
