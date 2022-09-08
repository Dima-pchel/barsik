package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;

public interface MessageCommandHandler {

    String command(Message eventMessage);

}
