package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.EmbedCommandHandler;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.exception.ExtractCommandException;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

@Component("signup")
public class SignupCommandHandler implements EmbedCommandHandler {

    private static final String COMMAND_NAME = "signup";

    @Resource
    private SignupService signupService;

    @Override
    public MessageEmbed command(Message eventMessage) throws EmbedCommandException {
        String content = eventMessage.getContentRaw();
        String subCommand = CommandHelper.extractMessage(content, COMMAND_NAME);
        try {
            Pair<String, String> commandPair = CommandHelper.extractCommand(subCommand);
            switch (commandPair.getLeft()) {
                // barsik create {name}
                case "create":
                    if (StringUtils.isEmpty(commandPair.getRight())) {
                        return signupService.createSignup(eventMessage.getChannel());
                    }
                    // barsik add {role}
                case "add":
                    return signupService.addUser(eventMessage.getChannel(), eventMessage.getAuthor(), commandPair.getRight());
                case "show":
                    return signupService.getSignupByChannel(eventMessage.getChannel());
                case "date":
                    return signupService.setDate(eventMessage.getChannel(), commandPair.getRight());
                case "note":
                    return signupService.setNote(eventMessage.getChannel(), commandPair.getRight());
                case "image":
                    return signupService.setImage(eventMessage.getChannel(), commandPair.getRight());
            }
        } catch (ExtractCommandException e) {

        }
        throw new EmbedCommandException(ERROR_MESSAGE);
    }
}
