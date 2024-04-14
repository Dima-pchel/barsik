package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;

import static ru.kets.barsik.constant.Constants.CommandName.MEOW_COMMAND_NAME;

@Component(MEOW_COMMAND_NAME)
public class SayMeowCommandHandler implements MessageCommandHandler {
    @Override
    public String command(Message eventMessage) {
        return "https://tenor.com/view/lily-woof-meow-cat-gif-11908890";
    }
}
