package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.integrations.DogClient;

import static ru.kets.barsik.constant.Constants.CommandName.FRIEND_COMMAND_NAME;

@Component(FRIEND_COMMAND_NAME)
public class FriendCommandHandler implements MessageCommandHandler {

    @Autowired
    private DogClient dogClient;

    @Override
    public String command(Message eventMessage) {
        return dogClient.getImage();
    }
}
