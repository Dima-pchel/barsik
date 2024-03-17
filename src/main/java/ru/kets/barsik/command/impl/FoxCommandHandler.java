package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.integrations.FoxClient;

import static ru.kets.barsik.constant.Constants.CommandName.FOX_COMMAND_NAME;

@Component(FOX_COMMAND_NAME)
public class FoxCommandHandler implements MessageCommandHandler {
    @Autowired
    private FoxClient foxClient;

    @Override
    public String command(Message eventMessage) {
        return foxClient.getImage();
    }
}
