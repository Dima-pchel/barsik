package ru.kets.barsik.service;

public interface RouletteGameService {

    String createNewGame(String description, String channelId);

    String updateSlots(Integer shots, String channelId);

    String getGame(String channelId);

    String endGame(String channelId);

    String shot(String channelId, String userId);
}
