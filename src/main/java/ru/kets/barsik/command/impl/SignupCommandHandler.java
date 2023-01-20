package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.EmbedType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.EmbedCommandHandler;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.exception.ExtractCommandException;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

import static ru.kets.barsik.integrations.constant.Constants.*;

@Component("signup")
public class SignupCommandHandler implements EmbedCommandHandler {

    private static final String COMMAND_NAME = "signup";

    Logger LOG = LoggerFactory.getLogger(SignupCommandHandler.class);

    @Resource
    private SignupService signupService;

    @Override
    public MessageEmbed command(Message eventMessage) throws EmbedCommandException {
        String content = eventMessage.getContentRaw();
        String subCommand = CommandHelper.extractMessage(content, COMMAND_NAME);
        try {
            Pair<String, String> commandPair = CommandHelper.extractCommand(subCommand);
            switch (commandPair.getLeft()) {
                // barsik signup create {name}
                case "create":
                    return signupService.createSignup(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup save {name}
                case "save":
                    return signupService.saveSignupPattern(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup add {role}
                case "add":
                    return signupService.addUser(eventMessage.getChannel(), eventMessage.getAuthor().getId(), commandPair.getRight());
                // barsik signup exit
                case "exit":
                    return signupService.removeUser(eventMessage.getChannel(), eventMessage.getAuthor(), null);
                // barsik signup remove {role}
                case "remove":
                    return signupService.removeRole(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup role {roleName}
                case "role":
                    return signupService.addRole(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup show
                case "show":
                    return signupService.getSignupByChannel(eventMessage.getChannel());
                // barsik signup date yyyy-MM-dd HH:mm
                case "date":
                    return signupService.setDate(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup note {note}
                case "note":
                    return signupService.setNote(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup image {link}
                case "image":
                    return signupService.setImage(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup thumbnail {link}
                case "thumbnail":
                    return signupService.setThumbnail(eventMessage.getChannel(), commandPair.getRight());
                // barsik signup help
                case "help":
                    return getHelpEmbed();
            }
        } catch (ExtractCommandException e) {
            LOG.error(e.getMessage(), e);
        }
        throw new EmbedCommandException(ERROR_MESSAGE_WITH_HELP_ADVICE);
    }

    private MessageEmbed getHelpEmbed() {
        List<MessageEmbed.Field> fieldList = new ArrayList<>();

        fieldList.add(new MessageEmbed.Field("barsik signup create {name}", "- create new signup from pattern {name}. If pattern not found return empty signup.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup role {roleName}", "- create and add to signup new empty role with {roleName} name.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup save {name}", "- save current signup as pattern with name {name}.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup add {role}", "- attach current user to role with name {role}. If role not found or not empty - create new role with {role} name.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup exit", "- remove current user from all roles.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup remove {role}", "- remove role by name (first will be deleted empty role).", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup show", "- show current signup in channel.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup date yyyy-MM-dd HH:mm", "- add date to current signup.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup note {note}", "- add note to current signup.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup image {link}", "- add image to current signup.", false, false));
        fieldList.add(new MessageEmbed.Field("barsik signup thumbnail {link}", "- add thumbnail to current signup.", false, false));

        MessageEmbed.ImageInfo img = new MessageEmbed.ImageInfo(SMART_CAT_IMG_LINK, SMART_CAT_IMG_LINK, 200, 200);

        return new MessageEmbed(null, "Signup Helper",
                null, EmbedType.RICH, null, HELP_COLOR,
                null, null, null, null, null, img, fieldList);
    }
}
