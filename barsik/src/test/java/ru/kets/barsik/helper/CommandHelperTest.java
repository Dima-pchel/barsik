package ru.kets.barsik.helper;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import ru.kets.barsik.exception.ExtractCommandException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CommandHelperTest {

    @Test
    void extractMessage_returnsTextAfterCommandName() {
        String result = CommandHelper.extractMessage("barsik say hello world", "say");

        assertEquals("hello world", result);
    }

    @Test
    void extractMessage_collapsesRepeatedWhitespace() {
        String result = CommandHelper.extractMessage("barsik   say    hello   world", "say");

        assertEquals("hello   world", result);
    }

    @Test
    void extractMessage_returnsEmptyWhenNoMessagePart() {
        String result = CommandHelper.extractMessage("barsik say", "say");

        assertEquals("", result);
    }

    @Test
    void extractCommand_splitsNameAndArgument() throws ExtractCommandException {
        Pair<String, String> result = CommandHelper.extractCommand("ban <@123> reason text");

        assertEquals("ban", result.getLeft());
        assertEquals("<@123> reason text", result.getRight());
    }

    @Test
    void extractCommand_returnsEmptyArgumentWhenNoSpace() throws ExtractCommandException {
        Pair<String, String> result = CommandHelper.extractCommand("help");

        assertEquals("help", result.getLeft());
        assertEquals("", result.getRight());
    }

    @Test
    void extractCommand_throwsOnEmptyContent() {
        assertThrows(ExtractCommandException.class, () -> CommandHelper.extractCommand(""));
    }

    @Test
    void extractUser_stripsMentionSyntax() {
        String result = CommandHelper.extractUser("<@123456789>");

        assertEquals("123456789", result);
    }

    @Test
    void generateRandomNumber_isWithinBounds() {
        for (int i = 0; i < 200; i++) {
            int result = CommandHelper.generateRandomNumber(6);
            assertTrue(result >= 0 && result < 6, "Expected value in [0, 6) but got " + result);
        }
    }

    @Test
    void generateRandomNumber_isAlwaysZeroWhenMaxIsOne() {
        assertEquals(0, CommandHelper.generateRandomNumber(1));
    }
}
