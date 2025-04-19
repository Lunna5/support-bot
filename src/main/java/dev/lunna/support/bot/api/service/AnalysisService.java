package dev.lunna.support.bot.api.service;

import dev.lunna.support.bot.api.data.QuestionAnswer;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface AnalysisService {
    void analyzeQuestionAndReply(
            @NotNull final String content,
            @NotNull final Function<CharSequence, MessageCreateAction> reply
    );
}
