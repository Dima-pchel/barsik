package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.kets.barsik.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component("ban")
public class BanCommandHandler implements MessageCommandHandler {

    private List<String> banReasons = new ArrayList<>(Arrays.asList("Забаню %s за красивые глаза", "%s забанен за длинные усы", "ban %s for no reason"));

    private static final String COMMAND_NAME = "ban";

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContent();
        String user = CommandHelper.extractMessage(content, COMMAND_NAME);
        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            return String.format(getBanReason(), user);
        }
        return "ban <@" + eventMessage.getAuthor().get().getId().asString() + "> for broken hands";
    }

    private String getBanReason() {
        return banReasons.get((int) (Math.random() * (banReasons.size())));
    }
}
