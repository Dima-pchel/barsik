package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

@Component("say")
public class SayCommandHandler implements MessageCommandHandler {

    private static final String COMMAND_NAME = "say";

    @Override
    public String command(Message eventMessage) {
        String say = CommandHelper.extractMessage(eventMessage.getContentRaw(), COMMAND_NAME);
        if ("meow".equals(say)) {
            return "https://tenor.com/view/lily-woof-meow-cat-gif-11908890";
        }
        return StringUtils.isNotBlank(say) ? say : ERROR_MESSAGE;
    }
}
