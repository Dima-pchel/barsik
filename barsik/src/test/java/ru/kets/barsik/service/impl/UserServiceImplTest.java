package ru.kets.barsik.service.impl;

import net.dv8tion.jda.api.entities.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kets.barsik.repo.UserRepo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private static final String DISCORD_ID = "discord-1";

    @Mock
    private UserRepo userRepo;

    @Mock
    private User discordUser;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void updateMessageCount_incrementsExistingUsersCount() {
        ru.kets.barsik.repo.pojo.User existing = new ru.kets.barsik.repo.pojo.User(DISCORD_ID, "name");
        when(discordUser.getId()).thenReturn(DISCORD_ID);
        when(userRepo.findUserByDiscordId(DISCORD_ID)).thenReturn(existing);

        userService.updateMessageCount(discordUser);

        assertEquals(1L, existing.getMessageCount());
        verify(userRepo).save(existing);
    }

    @Test
    void updateMessageCount_createsUserWhenNotFound() {
        when(discordUser.getId()).thenReturn(DISCORD_ID);
        lenient().when(discordUser.getName()).thenReturn("newUser");
        lenient().when(discordUser.isBot()).thenReturn(false);
        when(userRepo.findUserByDiscordId(DISCORD_ID)).thenReturn(null);

        userService.updateMessageCount(discordUser);

        ArgumentCaptor<ru.kets.barsik.repo.pojo.User> captor = ArgumentCaptor.forClass(ru.kets.barsik.repo.pojo.User.class);
        verify(userRepo, times(2)).save(captor.capture());
        ru.kets.barsik.repo.pojo.User savedAfterIncrement = captor.getAllValues().get(1);
        assertEquals(DISCORD_ID, savedAfterIncrement.getDiscordId());
        assertEquals(1L, savedAfterIncrement.getMessageCount());
    }

    @Test
    void updateReportCount_incrementsAndReturnsNewCount() {
        ru.kets.barsik.repo.pojo.User existing = new ru.kets.barsik.repo.pojo.User(DISCORD_ID, "name");
        existing.setReportCount(2);
        when(discordUser.getId()).thenReturn(DISCORD_ID);
        when(userRepo.findUserByDiscordId(DISCORD_ID)).thenReturn(existing);

        int result = userService.updateReportCount(discordUser);

        assertEquals(3, result);
        verify(userRepo).save(existing);
    }

    @Test
    void updateBanCount_incrementsAndReturnsNewCount() {
        ru.kets.barsik.repo.pojo.User existing = new ru.kets.barsik.repo.pojo.User(DISCORD_ID, "name");
        existing.setBanCount(0);
        when(discordUser.getId()).thenReturn(DISCORD_ID);
        when(userRepo.findUserByDiscordId(DISCORD_ID)).thenReturn(existing);

        int result = userService.updateBanCount(discordUser);

        assertEquals(1, result);
        verify(userRepo).save(existing);
    }
}
