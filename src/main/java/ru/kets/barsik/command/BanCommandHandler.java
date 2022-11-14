package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.BanReasonRepo;
import ru.kets.barsik.repo.pojo.BanReason;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kets.barsik.helper.CommandHelper.generateRandomNumber;

@Component("ban")
public class BanCommandHandler implements MessageCommandHandler {

    Logger LOG = LoggerFactory.getLogger(BanCommandHandler.class);

    private static final String COMMAND_NAME = "ban";

    @Resource
    BanReasonRepo reasonRepo;

    @Override
    public String command(Message eventMessage) {
        if (1 == generateRandomNumber(15)) {
            return String.format(getBanReason(), "<@" + eventMessage.getAuthor().get().getId().asString() + ">");
        }
        String content = eventMessage.getContent();
        String user = CommandHelper.extractMessage(content, COMMAND_NAME);
        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            return String.format(getBanReason(), user);
        }

        return "<@" + eventMessage.getAuthor().get().getId().asString() + "> has been banned for having too broken hands";
    }

    private String getBanReason() {
        List<String> reasons = reasonRepo.findAll().stream()
                .map(BanReason::getText)
                .collect(Collectors.toList());

        return reasons.get((int) (Math.random() * (reasons.size())));
    }
}
