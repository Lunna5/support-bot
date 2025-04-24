package dev.lunna.support.bot.service;

import dev.lunna.support.bot.api.config.BotConfig;
import dev.lunna.support.bot.api.config.ConfigContainer;
import dev.lunna.support.bot.api.data.QuestionAnswer;
import dev.lunna.support.bot.api.service.AnalysisService;
import jakarta.inject.Inject;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.function.Function;

public class AnalysisServiceFileSystem implements AnalysisService {
    @Inject
    private ConfigContainer<BotConfig> configContainer;

    @Override
    public void analyzeQuestionAndReply(@NotNull String content, @NotNull Function<CharSequence, MessageCreateAction> reply) {
        Collection<QuestionAnswer> questionAnswers = configContainer.get().questionAnswers.values();

        for (QuestionAnswer questionAnswer : questionAnswers) {
            for (String catchphrase : questionAnswer.catchphrases()) {
                final var split = catchphrase.toLowerCase().split(" ");
                final var length = split.length;

                if (length == 0) continue;

                if (length == 1 && content.equalsIgnoreCase(catchphrase)) {
                    reply.apply(questionAnswer.answer()).queue();
                    return;
                }

                int count = 0;

                for (String s : split) {
                    if (content.toLowerCase().contains(s)) {
                        count++;
                    }
                }

                if (count == length) {
                    reply.apply(questionAnswer.answer()).queue();
                    return;
                }
            }
        }
    }
}
