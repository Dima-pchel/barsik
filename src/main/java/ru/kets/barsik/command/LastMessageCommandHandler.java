package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.LastMessage;

import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component("again")
public class LastMessageCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        String lastMessage = LastMessage.getLastMessage(eventMessage.getAuthor().get().getUsername());
        return StringUtils.isEmpty(lastMessage) ? ERROR_MESSAGE : lastMessage;
    }
}
