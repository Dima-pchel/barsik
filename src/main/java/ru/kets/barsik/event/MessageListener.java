package ru.kets.barsik.event;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;
import ru.kets.barsik.command.DiscordCommandHandler;

import java.util.Map;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;

public abstract class MessageListener {

    @Autowired
    Map<String, DiscordCommandHandler> commandMap;

    public Mono<Void> processCommand(Message eventMessage) {
        String mes = getMessage(eventMessage);
        return Mono.just(eventMessage)
                .filter(message -> message.getAuthor().map(user -> !user.isBot()).orElse(false))
                .filter(message -> StringUtils.isNotBlank(message.getContent())
                        && message.getContent().toLowerCase().startsWith(COMMAND_PREFIX))
                .flatMap(Message::getChannel)
                .flatMap(channel -> channel.createMessage(mes))
                .then();
    }

    private String getMessage(Message message) {
        String content = message.getContent();
        String[] commands = content.split(" ");
        if (commands.length > 1) {
            DiscordCommandHandler discordCommand = commandMap.get(commands[1]);
            if (discordCommand != null) {
                return discordCommand.command(message);
            }
        }
        return StringUtils.EMPTY;
    }

}