package dev.lunna.support.bot.api.config;

import dev.lunna.support.bot.api.data.QuestionAnswer;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Setting;

import java.util.Map;

@ConfigSerializable
public class BotConfig {
    @Setting
    public Map<String, QuestionAnswer> questionAnswers;
}
