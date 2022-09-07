package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface DiscordCommandHandler {

    String command(Message eventMessage);

}
