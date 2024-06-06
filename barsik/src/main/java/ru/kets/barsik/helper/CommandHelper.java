package ru.kets.barsik.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.StringUtils;
import ru.kets.barsik.exception.ExtractCommandException;

import java.util.Optional;

import static ru.kets.barsik.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

public class CommandHelper {

    public static String extractMessage(String content, String commandName) {
        String lowerContent = content.toLowerCase();
        String start = String.join(" ", COMMAND_PREFIX.toLowerCase(), commandName.toLowerCase());
        String message = content.substring(lowerContent.indexOf(start) + start.length());
        return message.trim();
    }

    public static Pair<String, String> extractCommand(String content) throws ExtractCommandException {
        if (StringUtils.isNotEmpty(content)) {
            String trim = content.trim();
            int index = trim.indexOf(" ");
            if (index > 0) {
                return Pair.of(trim.substring(0, index), trim.substring(index).trim());
            }
            return Pair.of(trim, StringUtils.EMPTY);
        } else {
            throw new ExtractCommandException();
        }
    }

    public static String extractUser(String user) {
        String extracted = StringUtils.remove(user, "<@");
        return StringUtils.remove(extracted, ">");
    }

    public static int generateRandomNumber(int max) {
        return (int) (Math.random() * max);
    }
}
