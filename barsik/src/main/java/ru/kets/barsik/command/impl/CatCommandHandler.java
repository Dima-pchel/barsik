package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.integrations.CatClient;

import static ru.kets.barsik.constant.Constants.CommandName.CAT_COMMAND_NAME;

@Component(CAT_COMMAND_NAME)
public class CatCommandHandler implements MessageCommandHandler {

    @Autowired
    CatClient catClient;

    @Override
    public String command(Message eventMessage) {
        return catClient.getImage();
    }
}
