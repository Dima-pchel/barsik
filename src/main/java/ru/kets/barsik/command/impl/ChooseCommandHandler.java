package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

@Component("choose")
public class ChooseCommandHandler implements MessageCommandHandler {
    private static final String COMMAND_NAME = "choose";

    @Override
    public String command(Message eventMessage) {
        String choose = CommandHelper.extractMessage(eventMessage.getContentRaw(), COMMAND_NAME);
        String[] variants = choose.split(",");
        if (variants.length > 0) {
            int index = (int) (Math.random() * (variants.length));
            return variants[index];
        }
        return ERROR_MESSAGE;
    }
}
