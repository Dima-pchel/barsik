package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;

@Component("speak")
public class SpeakCommandHandler implements DiscordCommandHandler {

    @Override
    public String command(Message eventMessage) {
        return "МЯУ!";
    }
}
