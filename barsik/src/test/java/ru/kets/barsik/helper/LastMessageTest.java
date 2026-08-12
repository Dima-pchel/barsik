package ru.kets.barsik.helper;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class LastMessageTest {

    @Test
    void getLastMessage_returnsNullForUnknownUser() {
        assertNull(LastMessage.getLastMessage("unknown-user-" + System.nanoTime()));
    }

    @Test
    void addLastMessage_storesMessagePerUser() {
        String user = "user-" + System.nanoTime();

        LastMessage.addLastMessage("hello", user);

        assertEquals("hello", LastMessage.getLastMessage(user));
    }

    @Test
    void addLastMessage_overwritesPreviousMessageForSameUser() {
        String user = "user-" + System.nanoTime();

        LastMessage.addLastMessage("first", user);
        LastMessage.addLastMessage("second", user);

        assertEquals("second", LastMessage.getLastMessage(user));
    }
}
