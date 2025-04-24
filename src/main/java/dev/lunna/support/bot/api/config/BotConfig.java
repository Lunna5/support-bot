package dev.lunna.support.bot.api.config;

import dev.lunna.support.bot.api.data.QuestionAnswer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;

@ConfigSerializable
public class BotConfig {
    @Setting
    public Map<String, QuestionAnswer> questionAnswers = Map.of(
            "question1", new QuestionAnswer("Question 1", new String[]{"Catchphrase 1"}, "Answer 1"),
            "question2", new QuestionAnswer("Question 2", new String[]{"Catchphrase 2"}, "Answer 2")
    );
}
