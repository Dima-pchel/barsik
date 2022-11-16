package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component("tag")
public class TagCommandHandler implements MessageCommandHandler {


    @Override
    public String command(Message eventMessage) {
        //todo сделать таг юзера по похожему имени + картинка
        return ERROR_MESSAGE;
    }
}
