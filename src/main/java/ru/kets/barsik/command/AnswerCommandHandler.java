package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.AnswerClient;

@Component("answer")
public class AnswerCommandHandler implements MessageCommandHandler {

    private static final String COMMAND_NAME = "answer";

    @Autowired
    private AnswerClient answerClient;

    @Override
    public String command(Message eventMessage) {
        return answerClient.getImage();
    }
}
