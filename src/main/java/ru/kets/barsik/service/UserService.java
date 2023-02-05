package ru.kets.barsik.service;

import net.dv8tion.jda.api.entities.User;

public interface UserService {

    void updateMessageCount(User user);

    int updateReportCount(User user);

    int updateBanCount(User discordUser);
}
