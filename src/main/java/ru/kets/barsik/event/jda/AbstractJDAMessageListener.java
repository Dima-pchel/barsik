package ru.kets.barsik.event.jda;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.exception.AbuseException;
import ru.kets.barsik.helper.LastMessage;
import ru.kets.barsik.repo.WordRepo;
import ru.kets.barsik.repo.pojo.Word;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

import static ru.kets.barsik.constant.Constants.*;

public class AbstractJDAMessageListener extends ListenerAdapter {
    Logger LOG = LoggerFactory.getLogger(AbstractJDAMessageListener.class);

    @Autowired
    Map<String, MessageCommandHandler> commandMap;

    @Resource
    WordRepo wordRepo;

    public void processCommand(Message msg, MessageChannelUnion channel) {
        try {
            validateMessage(msg);
            String content = msg.getContentRaw();
            LOG.debug(content);
            if(!msg.getAuthor().isBot()
                    && StringUtils.isNotBlank(content)
                    && content.toLowerCase().startsWith(COMMAND_PREFIX)) {
                channel.sendMessage(getMessage(msg)).queue();
            }
        } catch (AbuseException e) {
            channel.sendMessage(NO_U_EMOJI).queue();
        } catch (Exception e) {
            channel.sendMessage(ERROR_MESSAGE).queue();
        }

    }

    private void validateMessage(Message eventMessage) throws AbuseException {
        String content = eventMessage.getContentRaw();
        if (commandMap.keySet().stream().anyMatch(c -> content.toLowerCase().contains(c))) {
            return;
        }
        List<Word> abuseWords = wordRepo.findWordsByType(Word.Type.ABUSE);
        if (abuseWords.stream().anyMatch(w ->
                (content.toLowerCase().contains(COMMAND_PREFIX) || content.toLowerCase().contains(COMMAND_PREFIX_RU))
                        && content.toLowerCase().contains(w.getWord().toLowerCase()))) {
            throw new AbuseException();
        }
    }

    private String getMessage(Message message) {
        String content = message.getContentRaw();
        String[] commands = content.split(" ");
        if (commands.length > 1) {
            MessageCommandHandler discordCommand = commandMap.get(commands[1].toLowerCase());
            if (discordCommand != null) {
                String command = discordCommand.command(message);
                LastMessage.addLastMessage(command, message.getAuthor().getName());
                return command;
            }
        }
        return ERROR_MESSAGE;
    }
}
