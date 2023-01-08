package ru.kets.barsik.command;


import net.dv8tion.jda.api.entities.Message;

public interface MessageCommandHandler {

    String command(Message eventMessage);

}
