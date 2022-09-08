package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component("choose")
public class ChooseCommandHandler implements MessageCommandHandler {
    private static final String COMMAND_NAME = "choose";

    @Override
    public String command(Message eventMessage) {
        String choose = extractContent(eventMessage.getContent());
        String[] variants = choose.split(",");
        if (variants.length > 0) {
            int index = (int) (Math.random() * (variants.length));
            return variants[index];
        }
        return ERROR_MESSAGE;
    }

    private String extractContent(String content) {
        return Optional.ofNullable(content.toLowerCase())
                .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command, COMMAND_NAME))
                .map(String::trim).orElse(ERROR_MESSAGE);
    }

}
