package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.LinkRepo;
import ru.kets.barsik.repo.pojo.Link;

import javax.annotation.Resource;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static ru.kets.barsik.constant.Constants.CommandName.ANSWER_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component(ANSWER_COMMAND_NAME)
public class AnswerCommandHandler implements MessageCommandHandler {
    private static Map<Link.Type, Integer> answerMap;

    @Resource
    private LinkRepo linkRepo;
    static {
        answerMap = new LinkedHashMap<>();
        answerMap.put(Link.Type.YES, 49);
        answerMap.put(Link.Type.NO, 98);
        answerMap.put(Link.Type.MAYBE, 100);
    }

    @Override
    public String command(Message eventMessage) {
        int random = CommandHelper.generateRandomNumber(100);
        for (Map.Entry<Link.Type, Integer> entry : answerMap.entrySet()) {
            if(random <= entry.getValue()) {
                List<Link> links = linkRepo.findLinksByType(entry.getKey());
                Link link = links.get(CommandHelper.generateRandomNumber(links.size() - 1));
                return link.toString();
            }
        }
        return ERROR_MESSAGE;
    }
}
