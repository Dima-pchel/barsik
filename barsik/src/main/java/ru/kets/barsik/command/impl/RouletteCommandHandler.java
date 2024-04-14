package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.exception.ExtractCommandException;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.service.RouletteGameService;

import javax.annotation.Resource;

import static ru.kets.barsik.constant.Constants.CommandName.ROULETTE_COMMAND_NAME;

@Component(ROULETTE_COMMAND_NAME)
public class RouletteCommandHandler implements MessageCommandHandler {

    @Resource
    private RouletteGameService rouletteGameService;

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContentRaw();
        String subCommand = CommandHelper.extractMessage(content, ROULETTE_COMMAND_NAME);
        try {
            Pair<String, String> commandPair = CommandHelper.extractCommand(subCommand);
            String subcommand = commandPair.getLeft();
            if ("create".equals(subcommand)) {
                return rouletteGameService.createNewGame(commandPair.getRight(), eventMessage.getChannel().getId());
            } else if ("slots".equals(subcommand)) {
                return rouletteGameService.updateSlots(Integer.valueOf(commandPair.getRight()), eventMessage.getChannel().getId());
            } else if ("show".equals(subcommand)) {
                return rouletteGameService.getGame(eventMessage.getChannel().getId());
            } else if ("end".equals(subcommand)) {
                return rouletteGameService.endGame(eventMessage.getChannel().getId());
            } else {
                throw new ExtractCommandException();
            }
        } catch (Exception e) {
            return "The wrong command. Use the following templates:\n" +
                    "barsik " + ROULETTE_COMMAND_NAME + " create {description},\n" +
                    "barsik " + ROULETTE_COMMAND_NAME + " slots {number of slots},\n" +
                    "barsik " + ROULETTE_COMMAND_NAME + " show,\n" +
                    "barsik " + ROULETTE_COMMAND_NAME + " end";
        }
    }
}
