package dev.lunna.support.bot.command;

import com.google.inject.Inject;
import dev.lunna.support.bot.api.config.BotConfig;
import dev.lunna.support.bot.api.config.ConfigContainer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.incendo.cloud.Command;
import org.incendo.cloud.context.CommandContext;
import org.incendo.cloud.discord.jda5.JDA5CommandManager;
import org.incendo.cloud.discord.jda5.JDAInteraction;
import org.incendo.cloud.discord.jda5.ReplySetting;
import org.incendo.cloud.discord.slash.CommandScope;
import org.incendo.cloud.discord.slash.DiscordChoices;
import org.jetbrains.annotations.NotNull;

import static java.util.Objects.requireNonNull;
import static org.incendo.cloud.parser.standard.StringParser.stringParser;

public class FrequentlyAskedCommand {
    @Inject
    private ConfigContainer<BotConfig> configContainer;

    public Command<JDAInteraction> build(@NotNull final JDA5CommandManager<JDAInteraction> manager) {
        return manager.commandBuilder("faq")
                .required(
                        "key",
                        stringParser(),
                        DiscordChoices.strings(
                                configContainer.get().questionAnswers.keySet()
                        )
                ).handler(this::execute)
                .apply(CommandScope.guilds())
                .apply(ReplySetting.doNotDefer())
                .build();
    }

    private void execute(@NonNull CommandContext<JDAInteraction> commandContext) {
        final String key = commandContext.get("key");

        final var questionAnswer = configContainer.get().questionAnswers.get(key);

        if (questionAnswer == null) {
            requireNonNull(commandContext.sender().interactionEvent())
                    .reply("No such question answer found.")
                    .queue();

            return;
        }

        requireNonNull(commandContext.sender().interactionEvent()).reply(questionAnswer.answer()).queue();
    }
}
