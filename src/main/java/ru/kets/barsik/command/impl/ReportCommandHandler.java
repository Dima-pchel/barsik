package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;

@Component("report")
public class ReportCommandHandler implements MessageCommandHandler {

    @Override
    public String command(Message eventMessage) {
        //TODO добавить счетчик репортов по пользователям
        return "This incident has been reported to the proper authorities. \nThank you for your time";
    }
}
