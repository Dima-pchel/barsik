package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;

@Component("say")
public class SayCommandHandler implements DiscordCommandHandler {

    private static final String COMMAND_NAME = "say";

    @Override
    public String command(Message eventMessage) {
        return Optional.ofNullable(eventMessage.getContent())
                .map(command -> StringUtils.remove(command,COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command,COMMAND_NAME))
                .map(String::trim).orElse("мяу");
    }
}
