package dev.lunna.support.bot.api.service;

import org.jetbrains.annotations.NotNull;

import java.io.InputStream;

public interface ImageAnalysisService {
    @NotNull
    String analyzeImage(@NotNull final InputStream image);
}
