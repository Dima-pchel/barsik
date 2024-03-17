package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;

import static ru.kets.barsik.constant.Constants.CommandName.AVATAR_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;
import static ru.kets.barsik.constant.Constants.IMAGE_SIZE_PARAMETER;

@Component(AVATAR_COMMAND_NAME)
public class AvatarCommandHandler implements MessageCommandHandler {

    Logger LOG = LoggerFactory.getLogger(AvatarCommandHandler.class);

    @Override
    public String command(Message eventMessage) {
        String userId = CommandHelper.extractMessage(eventMessage.getContentRaw(), AVATAR_COMMAND_NAME);
        //TODO this
        if (StringUtils.isNotBlank(userId) && userId.startsWith("<@")) {
            eventMessage.getGuild().getMembers();
            for (Member memberMention : eventMessage.getGuild().getMembers()) {
                if (userId.contains(memberMention.getId())) {
                    User user = memberMention.getUser();
                    String avatarUrl = user.getAvatarId();
                    LOG.info(avatarUrl);
                    if (StringUtils.isNotEmpty(avatarUrl)) {
                        return user.getAvatarUrl() + IMAGE_SIZE_PARAMETER;
                    }
                }
            }
        }
        return ERROR_MESSAGE;
    }
}
