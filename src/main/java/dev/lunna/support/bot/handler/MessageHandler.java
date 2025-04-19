package dev.lunna.support.bot.handler;

import dev.lunna.support.bot.api.service.AnalysisService;
import dev.lunna.support.bot.api.service.ImageAnalysisService;
import jakarta.inject.Inject;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

public class MessageHandler extends ListenerAdapter {
    @Inject
    private AnalysisService analysis;
    @Inject
    private ImageAnalysisService imageAnalysis;

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;

        final var message = event.getMessage();

        analysis.analyzeQuestionAndReply(message.getContentDisplay(), message::reply);

        for (Message.Attachment attachment : message.getAttachments()) {
            if (!attachment.isImage()) continue;

            if (attachment.getSize() > 8 * 1024 * 1024) {
                message.addReaction(Emoji.fromFormatted("⚠️")).queue();
            }

            message.addReaction(Emoji.fromFormatted("👀")).queue();

            attachment.getProxy().download().thenAccept(download -> {
                analysis.analyzeQuestionAndReply(imageAnalysis.analyzeImage(download), message::reply);
            });
        }
    }
}
