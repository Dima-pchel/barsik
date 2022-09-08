package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.springframework.stereotype.Component;

@Component("report")
public class ReportCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        //TODO добавить счетчик репортов по пользователям
        return "This incident has been reported to the proper authorities. \nThank you for your time";
    }
}
