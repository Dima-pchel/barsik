package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.service.LinkService;

import javax.annotation.Resource;

import static ru.kets.barsik.constant.Constants.CommandName.LINK_COMMAND_NAME;

@Component(LINK_COMMAND_NAME)
public class LinkCommandHandler implements MessageCommandHandler {
    Logger LOG = LoggerFactory.getLogger(LinkCommandHandler.class);

    @Resource
    private LinkService linkService;

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContentRaw();
        String subCommand = CommandHelper.extractMessage(content, LINK_COMMAND_NAME);
        try {
            Pair<String, String> commandPair = CommandHelper.extractCommand(subCommand);
            switch (commandPair.getLeft()) {
                // barsik link add [yes] {description} http://link.test
                case "add":
                    return linkService.createAndSaveLink(commandPair.getRight());
                // barsik link remove 123456
                case "remove":
                    return linkService.removeLink(commandPair.getRight());
                // barsik link show
                case "show":
                    return linkService.showAll();
            }
        } catch (Exception e) {
            LOG.error("cannot process link command due {}", e.getMessage(), e);
        }
        return "The wrong command. Use the following templates:\n" +
                "barsik " + LINK_COMMAND_NAME + " add [yes] {description} http://link.test,\n" +
                "barsik " + LINK_COMMAND_NAME + " remove 123456,\n" +
                "barsik " + LINK_COMMAND_NAME + " show.";
    }
}
