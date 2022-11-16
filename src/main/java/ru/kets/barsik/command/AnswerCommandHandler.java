package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.AnswerClient;

@Component("answer")
public class AnswerCommandHandler implements MessageCommandHandler {

    @Autowired
    private AnswerClient answerClient;

    @Override
    public String command(Message eventMessage) {
        return answerClient.getImage();
    }
}
