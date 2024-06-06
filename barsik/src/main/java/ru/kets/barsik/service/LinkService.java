package ru.kets.barsik.service;

public interface LinkService {

    String createAndSaveLink(String linkPattern);

    String removeLink(String id);

    String showAll();
}
