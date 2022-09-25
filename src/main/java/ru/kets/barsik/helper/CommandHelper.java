package ru.kets.barsik.helper;

import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

import static ru.kets.barsik.constant.Constants.COMMAND_PREFIX;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

public class CommandHelper {

    public static String extractMessage(String content, String commandName) {
        return Optional.ofNullable(content)
                .map(String::toLowerCase)
                .map(command -> StringUtils.remove(command, COMMAND_PREFIX))
                .map(command -> StringUtils.remove(command, commandName))
                .map(String::trim).orElse(ERROR_MESSAGE);
    }

    public static int generateRandomNumber(int max) {
         return (int) (Math.random() * max);
    }
}
