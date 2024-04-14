package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;

import static ru.kets.barsik.constant.Constants.CommandName.SAY_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component(SAY_COMMAND_NAME)
public class SayCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        String say = CommandHelper.extractMessage(eventMessage.getContentRaw(), SAY_COMMAND_NAME);
        if ("meow".equals(say)) {
            return "https://tenor.com/view/lily-woof-meow-cat-gif-11908890";
        }
        return StringUtils.isNotBlank(say) ? say : ERROR_MESSAGE;
    }
}
