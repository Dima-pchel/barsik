package ru.kets.barsik.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.StringUtils;
import ru.kets.barsik.exception.ExtractCommandException;

import java.util.Optional;

import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

public class CommandHelper {

    public static String extractMessage(String content, String commandName) {
        String[] parts = content.trim().split("\\s+", 3);
        return parts.length > 2 ? parts[2].trim() : StringUtils.EMPTY;
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
