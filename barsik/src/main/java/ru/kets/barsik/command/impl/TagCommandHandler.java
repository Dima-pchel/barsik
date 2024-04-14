package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;

import static ru.kets.barsik.constant.Constants.CommandName.TAG_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component(TAG_COMMAND_NAME)
public class TagCommandHandler implements MessageCommandHandler {


    @Override
    public String command(Message eventMessage) {
        //todo сделать таг юзера по похожему имени + картинка
        return ERROR_MESSAGE;
    }
}
