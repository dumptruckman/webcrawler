package wood.poulos.webcrawler.util;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import ch.qos.logback.core.spi.ContextAwareBase;
import com.google.auto.service.AutoService;

/**
 * A simple Logback Configurator. Automatically registered as a service.
 */
@AutoService(Configurator.class)
public class LogbackConfigurator extends ContextAwareBase implements Configurator {

    /**
     * {@inheritDoc}
     */
    @Override
    public void configure(LoggerContext loggerContext) {
        addInfo("Setting up logging configuration.");

        PatternLayout layout = new PatternLayout();
        //layout.setPattern("%d{HH:mm:ss.SSS} [%thread/%highlight(%level)]: [%logger{15}] %msg%n");
        layout.setPattern("[%gray(%thread)/%highlight(%level)]: %cyan([%logger{15}]) %msg%n");
        layout.setContext(loggerContext);
        layout.start();

        LayoutWrappingEncoder<ILoggingEvent> encoder = new LayoutWrappingEncoder<>();
        encoder.setContext(loggerContext);
        encoder.setLayout(layout);

        ConsoleAppender<ILoggingEvent> appender = new ConsoleAppender<>();
        appender.setContext(loggerContext);
        appender.setWithJansi(true);
        appender.setName("console");
        appender.setEncoder(encoder);
        appender.start();

        Logger rootLogger = loggerContext.getLogger(Logger.ROOT_LOGGER_NAME);
        rootLogger.addAppender(appender);

        String logLevelName = System.getProperty("logLevel");
        if (logLevelName != null) {
            rootLogger.setLevel(Level.toLevel(logLevelName));
        }
    }
}
