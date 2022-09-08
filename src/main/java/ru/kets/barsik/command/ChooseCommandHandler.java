package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;

import java.util.Optional;

import static ru.kets.barsik.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component("choose")
public class ChooseCommandHandler implements MessageCommandHandler {
    private static final String COMMAND_NAME = "choose";

    @Override
    public String command(Message eventMessage) {
        String choose = CommandHelper.extractMessage(eventMessage.getContent(), COMMAND_NAME);
        String[] variants = choose.split(",");
        if (variants.length > 0) {
            int index = (int) (Math.random() * (variants.length));
            return variants[index];
        }
        return ERROR_MESSAGE;
    }
}
