package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;

@Component("speak")
public class SpeakCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        return "What is the meaning of my life? \n" +
                "Master";
    }
}
