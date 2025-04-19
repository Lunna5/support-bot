package dev.lunna.support.bot.bootstrap;

import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import dev.lunna.support.bot.annotation.BotConfigPath;
import dev.lunna.support.bot.annotation.BotToken;
import dev.lunna.support.bot.api.config.BotConfig;
import dev.lunna.support.bot.api.config.ConfigContainer;
import dev.lunna.support.bot.api.service.AnalysisService;
import dev.lunna.support.bot.api.service.ImageAnalysisService;
import dev.lunna.support.bot.service.AnalysisServiceFileSystem;
import dev.lunna.support.bot.service.ImageAnalysisServiceTess4j;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public final class BotModule extends AbstractModule {
    private final String token;

    public BotModule(@NotNull final String token) {
        this.token = token;
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(BotToken.class).toInstance(token);

        bind(AnalysisService.class).to(AnalysisServiceFileSystem.class).asEagerSingleton();
        bind(ImageAnalysisService.class).to(ImageAnalysisServiceTess4j.class).asEagerSingleton();

        bind(new TypeLiteral<AtomicReference<ShardManager>>() {
        }).toInstance(new AtomicReference<>(null));

        try {
            final String dir = System.getProperty("user.dir");

            bind(new TypeLiteral<ConfigContainer<BotConfig>>() {
            }).toInstance(ConfigContainer.load(Path.of(dir), BotConfig.class, "config.yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
