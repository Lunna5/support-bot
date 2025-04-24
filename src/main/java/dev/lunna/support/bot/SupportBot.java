package dev.lunna.support.bot;

import com.google.inject.Injector;
import dev.lunna.support.bot.annotation.BotToken;
import dev.lunna.support.bot.command.FrequentlyAskedCommand;
import dev.lunna.support.bot.handler.MessageHandler;
import jakarta.inject.Inject;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.incendo.cloud.discord.jda5.JDA5CommandManager;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.incendo.cloud.execution.ExecutionCoordinator;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicReference;

public final class SupportBot extends ListenerAdapter {
    private static final Logger log = LoggerFactory.getLogger(SupportBot.class);
    @Inject
    private Injector injector;

    @Inject
    private AtomicReference<ShardManager> shardManager;

    @Inject
    @BotToken
    private String token;

    public void run() {
        Thread.currentThread().setName("SupportBot");

        final JDA5CommandManager<JDAInteraction> commandManager = new JDA5CommandManager<>(
                ExecutionCoordinator.asyncCoordinator(),
                JDAInteraction.InteractionMapper.identity()
        );

        FrequentlyAskedCommand command = injector.getInstance(FrequentlyAskedCommand.class);
        commandManager.command(command.build(commandManager));

        final var api = DefaultShardManagerBuilder
                .createDefault(token)
                .setAutoReconnect(true)
                .addEventListeners(this)
                .addEventListeners(commandManager.createListener())
                .addEventListeners(injector.getInstance(MessageHandler.class))
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        shardManager.set(api);

        try {
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        log.info("Logged in as {}#{}", event.getJDA().getSelfUser().getName(), event.getJDA().getSelfUser().getDiscriminator());
    }
}
