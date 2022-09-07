package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ru.kets.barsik.Constants.COMMAND_PREFIX;

@Component("ban")
public class BanCommandHandler implements DiscordCommandHandler {

    private List<String> banReasons = new ArrayList<>(Arrays.asList("Забаню %s за красивые глаза", "%s забанен за длинные усы", "ban %s for no reason"));

    private static final String COMMAND_NAME = "ban";

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContent();
        String user = extractUser(content);
        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            return String.format(getBanReason(), user);
        }
        return "ban <@" + eventMessage.getAuthor().get().getId().asString() + "> for broken hands";
    }

    private String extractUser(String content) {
        return Optional.ofNullable(content.toLowerCase())
                .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command, COMMAND_NAME))
                .map(String::trim).orElse("мяу");
    }

    private String getBanReason() {
        return banReasons.get((int) (Math.random() * (banReasons.size())));
    }
}
