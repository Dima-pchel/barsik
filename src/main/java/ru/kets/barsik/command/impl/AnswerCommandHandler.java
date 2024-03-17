package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.integrations.AnswerClient;

import static ru.kets.barsik.constant.Constants.CommandName.ANSWER_COMMAND_NAME;

@Component(ANSWER_COMMAND_NAME)
public class AnswerCommandHandler implements MessageCommandHandler {

    @Autowired
    private AnswerClient answerClient;

    @Override
    public String command(Message eventMessage) {
        return answerClient.getImage();
    }
}
