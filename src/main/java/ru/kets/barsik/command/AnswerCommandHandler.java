package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.AnswerClient;

import java.util.Optional;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component("answer")
public class AnswerCommandHandler implements MessageCommandHandler {

    private static final String COMMAND_NAME = "answer";
    @Autowired
    private AnswerClient answerClient;

    @Override
    public String command(Message eventMessage) {
        String image = answerClient.getImage();
        if (StringUtils.isNotBlank(image) && image.startsWith("yes")) {
            String answer = Optional.ofNullable(eventMessage.getContent())
                    .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                    .map(command -> StringUtils.remove(command, COMMAND_NAME))
                    .map(String::trim).orElse(ERROR_MESSAGE);
            return image + "\n" + StringUtils.remove(answer, "?");
        }
        return image;
    }
}
