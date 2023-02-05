package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.BanReasonRepo;
import ru.kets.barsik.repo.pojo.BanReason;
import ru.kets.barsik.service.UserService;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kets.barsik.helper.CommandHelper.generateRandomNumber;

@Component("ban")
public class BanCommandHandler implements MessageCommandHandler {
    private static final String COMMAND_NAME = "ban";

    @Resource
    private BanReasonRepo reasonRepo;

    @Resource
    private UserService userService;

    @Override
    public String command(Message eventMessage) {
        if (1 == generateRandomNumber(15)) {
            userService.updateBanCount(eventMessage.getAuthor());
            return String.format(getBanReason(), "<@" + eventMessage.getAuthor().getId() + ">");
        }
        String content = eventMessage.getContentRaw();
        String user = CommandHelper.extractMessage(content, COMMAND_NAME);
        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            String userId = CommandHelper.extractUser(user);
            User discordUser = eventMessage.getGuild().getMemberById(userId).getUser();
            userService.updateBanCount(discordUser);
            return String.format(getBanReason(), user);
        }

        return "<@" + eventMessage.getAuthor().getId() + "> has been banned for having too broken hands";
    }

    private String getBanReason() {
        List<String> reasons = reasonRepo.findAll().stream()
                .map(BanReason::getText)
                .collect(Collectors.toList());

        return reasons.get((int) (Math.random() * (reasons.size())));
    }
}
