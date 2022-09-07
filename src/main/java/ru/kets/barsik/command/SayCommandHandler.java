package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component("say")
public class SayCommandHandler implements MessageCommandHandler {

    private static final String COMMAND_NAME = "say";

    @Override
    public String command(Message eventMessage) {
        String say = Optional.ofNullable(eventMessage.getContent())
                .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command, COMMAND_NAME))
                .map(String::trim).orElse(ERROR_MESSAGE);
        if("meow".equals(say)) {
            return "https://tenor.com/view/lily-woof-meow-cat-gif-11908890";
        }
        return say;
    }
}
