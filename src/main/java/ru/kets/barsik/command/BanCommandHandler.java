package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static ru.kets.barsik.helper.CommandHelper.generateRandomNumber;

@Component("ban")
public class BanCommandHandler implements MessageCommandHandler {

    Logger LOG = LoggerFactory.getLogger(BanCommandHandler.class);

    private List<String> banReasons = new ArrayList<>(Arrays.asList("%s has been banned for being too awesome",
            "%s has been banned for not being Kulon",
            "%s has been banned for having too fluffy tail",
            "%s has been banned for eating too much pizza",
            "%s has been banned for not enough slacking",
            "%s has been banned for not being cat",
            "%s has been banned for having fun",
            "%s has been banned for being bad cat \n" +
                    "https://cdn.discordapp.com/attachments/822912238519058462/1021839876620042421/6474ec4879642040.png"));

    private static final String COMMAND_NAME = "ban";

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContent();
        if (1 == generateRandomNumber(10)) {
            return String.format(getBanReason(), "<@" + eventMessage.getAuthor().get().getId().asString() + ">");
        }
        String user = CommandHelper.extractMessage(content, COMMAND_NAME);
        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            return String.format(getBanReason(), user);
        }

        return "<@" + eventMessage.getAuthor().get().getId().asString() + "> has been banned for having too broken hands";
    }

    private String getBanReason() {
        return banReasons.get((int) (Math.random() * (banReasons.size())));
    }
}
