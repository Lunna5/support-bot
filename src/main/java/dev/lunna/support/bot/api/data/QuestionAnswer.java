package dev.lunna.support.bot.api.data;

import org.spongepowered.configurate.objectmapping.ConfigSerializable;

@ConfigSerializable
public record QuestionAnswer(
        String identifier,
        String[] catchphrases,
        String answer
) {
}
