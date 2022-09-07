package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import discord4j.core.object.entity.PartialMember;
import discord4j.rest.util.Image;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component("avatar")
public class AvatarCommandHandler implements MessageCommandHandler {

    Logger LOG = LoggerFactory.getLogger(AvatarCommandHandler.class);

    private static final String COMMAND_NAME = "avatar";

    @Override
    public String command(Message eventMessage) {
        String user = extractUser(eventMessage.getContent());
        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            for (PartialMember memberMention : eventMessage.getMemberMentions()) {
                if (user.contains(memberMention.getId().asString())) {
                    LOG.info(memberMention.getAvatarUrl());
                    Optional<String> avatarUrl = memberMention.getAvatarUrl(Image.Format.WEB_P);
                    if (avatarUrl.isPresent()) {
                        return avatarUrl.get() + "?size=480";
                    }
                }
            }
        }
        return ERROR_MESSAGE;
    }

    private String extractUser(String content) {
        return Optional.ofNullable(content)
                .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command, COMMAND_NAME))
                .map(String::trim).orElse(ERROR_MESSAGE);
    }
}
