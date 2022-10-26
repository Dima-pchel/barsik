package ru.kets.barsik.command;

import discord4j.core.object.entity.Message;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.WordRepo;
import ru.kets.barsik.repo.pojo.Word;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Component("abuseadd")
public class AbuseWordsCommandHandler implements MessageCommandHandler{

    @Resource
    WordRepo wordRepo;
    private static final String COMMAND_NAME = "abuseadd";

    @Override
    public String command(Message eventMessage) {
        String message = CommandHelper.extractMessage(eventMessage.getContent(), COMMAND_NAME);
        String[] split = message.split(",");
        List<Word> words = new ArrayList<>();
        for (String w : split) {
            Word word = new Word();
            word.setWord(removeInvertedCommas(trimUnicodeSurrogateCharacters(w)));
            word.setType(Word.Type.ABUSE);
            words.add(word);
        }
        wordRepo.saveAll(words);
        return "OK";
    }

    private String removeInvertedCommas(String word) {
        return StringUtils.remove(word, "\"").trim();
    }

    public static String trimUnicodeSurrogateCharacters(String message) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            if (!Character.isHighSurrogate(ch) && !Character.isLowSurrogate(ch)) {
                sb.append(ch);
            }
        }
        return sb.toString();
    }
}