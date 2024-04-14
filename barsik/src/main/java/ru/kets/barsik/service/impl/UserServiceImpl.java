package ru.kets.barsik.service.impl;

import org.springframework.stereotype.Service;
import ru.kets.barsik.repo.UserRepo;
import ru.kets.barsik.repo.pojo.User;
import ru.kets.barsik.service.UserService;

import javax.annotation.Resource;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    UserRepo userRepo;

    @Override
    public void updateMessageCount(net.dv8tion.jda.api.entities.User user) {
        User userByDiscordId = getUser(user);
        userByDiscordId.setMessageCount(userByDiscordId.getMessageCount() + 1);

        userRepo.save(userByDiscordId);
    }

    @Override
    public int updateReportCount(net.dv8tion.jda.api.entities.User user) {
        User userByDiscordId = getUser(user);
        userByDiscordId.setReportCount(userByDiscordId.getReportCount() + 1);

        userRepo.save(userByDiscordId);
        return userByDiscordId.getReportCount();
    }

    @Override
    public int updateBanCount(net.dv8tion.jda.api.entities.User discordUser) {
        User userByDiscordId = getUser(discordUser);
        userByDiscordId.setBanCount(userByDiscordId.getBanCount() + 1);

        userRepo.save(userByDiscordId);
        return userByDiscordId.getBanCount();
    }

    private User getUser(net.dv8tion.jda.api.entities.User user) {
        User userByDiscordId = userRepo.findUserByDiscordId(user.getId());
        if (userByDiscordId == null) {
            userByDiscordId = createUser(user);
        }
        return userByDiscordId;
    }

    private User createUser(net.dv8tion.jda.api.entities.User discordUser) {
        User user = new User(discordUser.getId(), discordUser.getName());
        user.setBot(discordUser.isBot());
        userRepo.save(user);
        return user;
    }
}
