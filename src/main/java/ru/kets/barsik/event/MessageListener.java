package ru.kets.barsik.event;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import ru.kets.barsik.command.AvatarCommandHandler;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.LastMessage;

import java.util.Map;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.Constants.ERROR_MESSAGE;

public abstract class MessageListener {
    Logger LOG = LoggerFactory.getLogger(MessageListener.class);

    @Autowired
    Map<String, MessageCommandHandler> commandMap;

    public Mono<Void> processCommand(Message eventMessage) {
        try {


            String mes = getMessage(eventMessage);
            return Mono.just(eventMessage)
                    .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                    .filter(message -> StringUtils.isNotBlank(message.getContent())
                            && message.getContent().toLowerCase().startsWith(COMMAND_PREFIX))
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(mes))
                    .then();
        } catch (Exception e) {
            LOG.error("Capitan we have a problem :" + e.getMessage(), e);
            return Mono.just(eventMessage)
                    .flatMap(Message::getChannel)
                    .flatMap(channel -> channel.createMessage(ERROR_MESSAGE))
                    .then();
        }
    }

    private String getMessage(Message message) {
        String content = message.getContent();
        String[] commands = content.split(" ");
        if (commands.length > 1) {
            MessageCommandHandler discordCommand = commandMap.get(commands[1]);
            if (discordCommand != null) {
                String command = discordCommand.command(message);
                LastMessage.addLastMessage(command, message.getAuthor().get().getUsername());
                return command;
            }
        }
        return "МЯУ!";
    }

}