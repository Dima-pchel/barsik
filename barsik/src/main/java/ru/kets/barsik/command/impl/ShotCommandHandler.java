package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.service.RouletteGameService;

import javax.annotation.Resource;

import static ru.kets.barsik.constant.Constants.CommandName.SHOT_COMMAND_NAME;

@Component(SHOT_COMMAND_NAME)
public class ShotCommandHandler implements MessageCommandHandler {

    @Resource
    private RouletteGameService rouletteGameService;

    @Override
    public String command(Message eventMessage) {
        return rouletteGameService.shot(eventMessage.getChannel().getId(), eventMessage.getAuthor().getId());
    }
}
