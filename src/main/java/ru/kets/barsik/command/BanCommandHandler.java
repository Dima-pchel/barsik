package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component("ban")
public class BanCommandHandler implements MessageCommandHandler {

    private List<String> banReasons = new ArrayList<>(Arrays.asList("%s has been banned for being too awesome",
            "%s has been banned for not being Kulon",
            "%s has been banned for having too fluffy tail",
            "%s has been banned for eating too much pizza",
            "%s has been banned for not enough slacking",
            "%s has been banned for not being cat",
            "%s has been banned for having fun",
            "%s has been banned for  being bad cat\n" +
                    "https://media.discordapp.net/attachments/822912238519058462/1021485106956865647/unknown.png"));

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
