package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.LastMessage;

import static ru.kets.barsik.constant.Constants.CommandName.AGAIN_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component(AGAIN_COMMAND_NAME)
public class LastMessageCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        String lastMessage = LastMessage.getLastMessage(eventMessage.getAuthor().getName());
        return StringUtils.isEmpty(lastMessage) ? ERROR_MESSAGE : lastMessage;
    }
}
