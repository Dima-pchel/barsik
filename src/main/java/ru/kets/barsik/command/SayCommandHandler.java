package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;

import java.util.Optional;

import static ru.kets.barsik.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component("say")
public class SayCommandHandler implements MessageCommandHandler {

    private static final String COMMAND_NAME = "say";

    @Override
    public String command(Message eventMessage) {
        String say = CommandHelper.extractMessage(eventMessage.getContent(), COMMAND_NAME);
        if("meow".equals(say)) {
            return "https://tenor.com/view/lily-woof-meow-cat-gif-11908890";
        }
        return StringUtils.isNotBlank(say) ? say : ERROR_MESSAGE;
    }
}
