package cz.cvut.fel.pjv.spacemechanic.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Sets one shared logging configuration for the whole game.
 */
public final class LogConfig {

    private static boolean configured;

    private LogConfig() {
    }

    public static void configure() {
        if (configured) {
            return;
        }

        System.setProperty(
                "java.util.logging.SimpleFormatter.format",
                "[%1$tT] [%4$s] %3$s - %5$s%6$s%n"
        );

        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);

        for (Handler handler : rootLogger.getHandlers()) {
            rootLogger.removeHandler(handler);
        }

        ConsoleHandler consoleHandler = new ConsoleHandler();
        consoleHandler.setLevel(Level.INFO);
        consoleHandler.setFormatter(new SimpleFormatter());
        rootLogger.addHandler(consoleHandler);

        configured = true;
    }
}
