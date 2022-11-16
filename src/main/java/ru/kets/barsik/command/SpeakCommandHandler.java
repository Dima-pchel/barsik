package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

@Component("speak")
public class SpeakCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        return "What is the meaning of my life? \n" +
                "Master";
    }
}
