package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import reactor.core.publisher.Mono;

public interface MessageCommandHandler {

    String command(Message eventMessage);

}
