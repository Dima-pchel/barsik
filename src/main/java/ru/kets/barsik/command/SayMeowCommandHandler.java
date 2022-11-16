package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;

@Component("meow")
public class SayMeowCommandHandler implements MessageCommandHandler {
    @Override
    public String command(Message eventMessage) {
        return "https://tenor.com/view/lily-woof-meow-cat-gif-11908890";
    }
}
