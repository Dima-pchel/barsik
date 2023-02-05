package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.service.UserService;

import javax.annotation.Resource;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

@Component("report")
public class ReportCommandHandler implements MessageCommandHandler {
    private static final String COMMAND_NAME = "report";

    @Resource
    private UserService userService;

    @Override
    public String command(Message eventMessage) {
        String content = eventMessage.getContentRaw();
        String user = CommandHelper.extractMessage(content, COMMAND_NAME);

        if (StringUtils.isNotBlank(user) && user.startsWith("<@")) {
            String userDiscordId = CommandHelper.extractUser(user);
            User userById = eventMessage.getGuild().getMemberById(userDiscordId).getUser();
            int count = userService.updateReportCount(userById);
            if (count % 10 == 0) {
                return String.format("This incident has been reported to the proper authorities. " +
                        "\nThank you for your time. \nUser was reported %s times", count);
            } else {
                return "This incident has been reported to the proper authorities. \nThank you for your time";
            }
        }
        return ERROR_MESSAGE;
    }
}
