package dev.lunna.support.bot.api.config;

import dev.lunna.support.bot.api.data.QuestionAnswer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.List;

@ConfigSerializable
public class BotConfig {
    @Setting
    public List<QuestionAnswer> questionAnswers = List.of(
            new QuestionAnswer("Question 1", new String[]{"Catchphrase 1"}, "Answer 1"),
            new QuestionAnswer("Question 2", new String[]{"Catchphrase 2"}, "Answer 2")
    );
}
