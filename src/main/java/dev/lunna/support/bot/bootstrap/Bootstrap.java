package dev.lunna.support.bot.bootstrap;

import com.google.inject.Guice;
import dev.lunna.support.bot.SupportBot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.util.concurrent.Callable;

@Command(name = "support-bot", mixinStandardHelpOptions = true, version = "1.0.0", description = "Support Bot")
public final class Bootstrap implements Callable<Integer> {
    private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

    @Parameters(index = "0", description = "The token to use for the bot.")
    private String token;

    @Override
    public Integer call() throws Exception {
        LOGGER.info("Injecting members...");
        final var injector = Guice.createInjector(new BotModule(token));
        injector.getInstance(SupportBot.class).run();
        return 0;
    }

    public static void main(String... args) {
        CommandLine commandLine = new CommandLine(new Bootstrap());
        commandLine.setCaseInsensitiveEnumValuesAllowed(true);
        commandLine.setExecutionStrategy(new CommandLine.RunLast());
        int exitCode = commandLine.execute(args);
        System.exit(exitCode);
    }
}
