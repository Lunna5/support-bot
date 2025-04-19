package dev.lunna.support.bot.service;

import dev.lunna.support.bot.api.service.ImageAnalysisService;
import dev.lunna.support.bot.bootstrap.Bootstrap;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class ImageAnalysisServiceTess4j implements ImageAnalysisService {
    private static final Logger log = LoggerFactory.getLogger(ImageAnalysisServiceTess4j.class);

    private final Tesseract tesseract = new Tesseract();

    public ImageAnalysisServiceTess4j() {
        final var currentDir = System.getProperty("user.dir");
        final var tess4jDir = Path.of(currentDir, "tess4j");
        final var english = new File(tess4jDir.toFile(), "eng.traineddata");

        if (!english.exists()) {
            try (final var inputStream = Bootstrap.class.getResourceAsStream("/eng.traineddata")) {
                if (inputStream == null) {
                    throw new RuntimeException("Failed to load eng.traineddata");
                }

                final var tess4jDirFile = tess4jDir.toFile();

                if (!tess4jDirFile.exists() && !tess4jDirFile.mkdirs()) {
                    throw new RuntimeException("Failed to create tess4j directory");
                }

                final var englishFile = new File(tess4jDirFile, "eng.traineddata");
                try (final var outputStream = java.nio.file.Files.newOutputStream(englishFile.toPath())) {
                    inputStream.transferTo(outputStream);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        tesseract.setDatapath(tess4jDir.toString());
        tesseract.setLanguage("eng");
        tesseract.setVariable("user_defined_dpi", "300");
    }

    @NotNull
    @Override
    public String analyzeImage(@NotNull InputStream image) {
        log.debug("Analyzing image...");

        final BufferedImage bufferedImage;

        try {
            bufferedImage = ImageIO.read(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            return tesseract.doOCR(bufferedImage);
        } catch (TesseractException e) {
            throw new RuntimeException(e);
        }
    }
}
