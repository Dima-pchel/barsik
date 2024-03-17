package ru.kets.barsik.event.d4j;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.exception.AbuseException;
import ru.kets.barsik.repo.WordRepo;
import ru.kets.barsik.repo.pojo.Word;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static ru.kets.barsik.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.constant.Constants.*;

@Deprecated
public abstract class MessageListener {
    Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    Map<String, MessageCommandHandler> commandMap;

    @Resource
    WordRepo wordRepo;

//    @Qualifier("getGatewayDiscordClient")
//    @Autowired
//    GatewayDiscordClient client;

    public Mono<Void> processCommand(Message eventMessage) {
        try {
            validateMessage(eventMessage);
            return Mono.just(eventMessage)
                    .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .filter(message -> StringUtils.isNotBlank(message.getContent())
                            && message.getContent().toLowerCase().startsWith(COMMAND_PREFIX))
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(getMessage(eventMessage)))
                    .then();
        } catch (AbuseException ae) {
            return sendAnswer(eventMessage, NO_U_EMOJI);
        } catch (Exception e) {
            LOG.error("Capitan we have a problem :" + e.getMessage(), e);
            return sendAnswer(eventMessage, ERROR_MESSAGE);
        }
    }

    private static Mono<Void> sendAnswer(Message eventMessage, String message) {
        return Mono.just(eventMessage)
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(message))
                .then();
    }

    private void validateMessage(Message eventMessage) throws AbuseException {
        String content = eventMessage.getContent();
        if (commandMap.keySet().stream().anyMatch(c -> content.toLowerCase().contains(c))) {
            return;
        }
        List<Word> abuseWords = wordRepo.findWordsByType(Word.Type.ABUSE);
        if (abuseWords.stream().anyMatch(w ->
                (content.toLowerCase().contains(COMMAND_PREFIX) || content.toLowerCase().contains(COMMAND_PREFIX_RU))
                        && content.toLowerCase().contains(w.getWord().toLowerCase()))) {
            throw new AbuseException();
        }
    }

    private String getMessage(Message message) {
//        String content = message.getContent();
//        String[] commands = content.split(" ");
//        if (commands.length > 1) {
//            MessageCommandHandler discordCommand = commandMap.get(commands[1].toLowerCase());
//            if (discordCommand != null) {
//                String command = discordCommand.command(message);
//                LastMessage.addLastMessage(command, message.getAuthor().get().getUsername());
//                return command;
//            }
//        }
        return ERROR_MESSAGE;
    }

}