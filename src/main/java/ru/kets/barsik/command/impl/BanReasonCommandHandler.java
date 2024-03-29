package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.exception.ExtractCommandException;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.BanReasonRepo;
import ru.kets.barsik.repo.pojo.BanReason;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.kets.barsik.constant.Constants.CommandName.BAN_REASON_COMMAND_NAME;

@Component(BAN_REASON_COMMAND_NAME)
public class BanReasonCommandHandler implements MessageCommandHandler {

    @Resource
    BanReasonRepo banReasonRepo;

    @Override
    public String command(Message eventMessage) {
        // barsik banreason add %s is not good man!!
        String content = eventMessage.getContentRaw();
        String subCommand = CommandHelper.extractMessage(content, BAN_REASON_COMMAND_NAME);
        try {
            Pair<String, String> commandPair = CommandHelper.extractCommand(subCommand);
            String command = commandPair.getLeft();
            if ("view".equals(command)) {
                List<String> all = banReasonRepo.findAll()
                        .stream()
                        .map(BanReason::toString)
                        .collect(Collectors.toList());
                return String.join("\n", all);
            } else if ("add".equals(command)) {
                if (StringUtils.isEmpty(commandPair.getRight())) {
                    throw new ExtractCommandException();
                }
                BanReason reason = new BanReason();
                reason.setText(commandPair.getRight());
                banReasonRepo.save(reason);
            } else if ("remove".equals(command)) {
                if (StringUtils.isEmpty(commandPair.getRight())) {
                    throw new ExtractCommandException();
                }
                try {
                    long id = Long.parseLong(commandPair.getRight());
                    banReasonRepo.deleteAllById(Collections.singletonList(id));
                } catch (NumberFormatException exception) {
                    throw new ExtractCommandException();
                }
            } else {
                throw new ExtractCommandException();
            }
        } catch (ExtractCommandException e) {
            return "Available commands: \n" +
                    " - view (returns all ban reasons in format {ID} - {message}) \n" +
                    " - add {message} (add new reasons %s replace with banned user tag) \n" +
                    " - remove {ID} (remove reason by id}";
        }
        return "done!";
    }
}
