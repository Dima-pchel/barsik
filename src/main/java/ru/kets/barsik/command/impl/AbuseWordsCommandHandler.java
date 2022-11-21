package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.WordRepo;
import ru.kets.barsik.repo.pojo.Word;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("abuseadd")
public class AbuseWordsCommandHandler implements MessageCommandHandler {

    @Resource
    WordRepo wordRepo;
    private static final String COMMAND_NAME = "abuseadd";

    @Override
    public String command(Message eventMessage) {
        String message = CommandHelper.extractMessage(eventMessage.getContentRaw(), COMMAND_NAME);
        String[] split = message.split(",");
        List<Word> words = new ArrayList<>();
        for (String w : split) {
            Word word = new Word();
            word.setWord(removeInvertedCommas(w));
            word.setType(Word.Type.ABUSE);
            words.add(word);
        }
        wordRepo.saveAll(words);
        return "OK";
    }

    private String removeInvertedCommas(String word) {
        return StringUtils.remove(word, "\"").trim();
    }
}
