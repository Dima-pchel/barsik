package ru.kets.barsik.helper;

import discord4j.core.object.entity.Message;

import java.util.HashMap;
import java.util.Map;

public class LastMessage {

    private static Map<String, String> lastMessages = new HashMap<>();

    public static void addLastMessage(String message, String user) {
        lastMessages.put(user, message);
    }

    public static String getLastMessage(String user) {
        return lastMessages.get(user);
    }
}
