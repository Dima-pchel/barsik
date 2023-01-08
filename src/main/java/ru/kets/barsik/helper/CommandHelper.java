package ru.kets.barsik.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.StringUtils;
import ru.kets.barsik.exception.ExtractCommandException;

import java.util.Optional;

import static ru.kets.barsik.integrations.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

public class CommandHelper {

    public static String extractMessage(String content, String commandName) {
        return Optional.ofNullable(content)
                .map(String::toLowerCase)
                .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command, commandName))
                .map(String::trim).orElse(ERROR_MESSAGE);
    }

    public static Pair<String, String> extractCommand(String content) throws ExtractCommandException {
        if (StringUtils.isNotEmpty(content)) {
            String trim = content.trim();
            int index = trim.indexOf(" ");
            if( index > 0) {
                return Pair.of(trim.substring(0, index), trim.substring(index).trim());
            }
            return Pair.of(trim, StringUtils.EMPTY);
        } else {
            throw new ExtractCommandException();
        }
    }

    public static int generateRandomNumber(int max) {
        return (int) (Math.random() * max);
    }
}
