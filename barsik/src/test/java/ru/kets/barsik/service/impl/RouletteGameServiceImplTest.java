package ru.kets.barsik.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.RouletteGameRepo;
import ru.kets.barsik.repo.pojo.RouletteGame;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RouletteGameServiceImplTest {

    private static final String CHANNEL_ID = "channel-1";
    private static final String USER_ID = "user-1";

    @Mock
    private RouletteGameRepo rouletteGameRepo;

    @InjectMocks
    private RouletteGameServiceImpl rouletteGameService;

    private MockedStatic<CommandHelper> commandHelper;

    @BeforeEach
    void setUp() {
        commandHelper = mockStatic(CommandHelper.class);
    }

    @AfterEach
    void tearDown() {
        commandHelper.close();
    }

    @Test
    void createNewGame_deactivatesExistingActiveGameAndCreatesNew() {
        RouletteGame oldGame = new RouletteGame("old", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(oldGame));

        rouletteGameService.createNewGame("new game", CHANNEL_ID);

        assertFalse(oldGame.isActive());
        ArgumentCaptor<RouletteGame> captor = ArgumentCaptor.forClass(RouletteGame.class);
        verify(rouletteGameRepo, times(2)).save(captor.capture());
        assertEquals(oldGame, captor.getAllValues().get(0));
        RouletteGame newGame = captor.getAllValues().get(1);
        assertEquals("new game", newGame.getDescription());
        assertTrue(newGame.isActive());
    }

    @Test
    void createNewGame_savesOnlyNewGameWhenNoneActive() {
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.empty());

        rouletteGameService.createNewGame("new game", CHANNEL_ID);

        verify(rouletteGameRepo, times(1)).save(any());
    }

    @Test
    void updateSlots_setsExactValueWhenAtOrBelowSix() {
        RouletteGame game = new RouletteGame("desc", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(game));

        rouletteGameService.updateSlots(4, CHANNEL_ID);

        assertEquals(4, game.getShootsNumbers());
    }

    @Test
    void updateSlots_capsAtSixWhenAboveSix() {
        RouletteGame game = new RouletteGame("desc", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(game));

        rouletteGameService.updateSlots(10, CHANNEL_ID);

        assertEquals(6, game.getShootsNumbers());
    }

    @Test
    void updateSlots_returnsNotFoundMessageWhenNoActiveGame() {
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.empty());

        String result = rouletteGameService.updateSlots(4, CHANNEL_ID);

        assertEquals("Game was not found =(", result);
    }

    @Test
    void getGame_returnsGameDescriptionWhenFound() {
        RouletteGame game = new RouletteGame("desc", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(game));

        assertEquals(game.toString(), rouletteGameService.getGame(CHANNEL_ID));
    }

    @Test
    void getGame_returnsNotFoundMessageWhenAbsent() {
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.empty());

        assertEquals("Game was not found =(", rouletteGameService.getGame(CHANNEL_ID));
    }

    @Test
    void endGame_deactivatesAndSaves() {
        RouletteGame game = new RouletteGame("desc", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(game));

        String result = rouletteGameService.endGame(CHANNEL_ID);

        assertFalse(game.isActive());
        verify(rouletteGameRepo).save(game);
        assertEquals("Game over", result);
    }

    @Test
    void endGame_returnsNotFoundMessageWhenAbsent() {
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.empty());

        assertEquals("Game was not found =(", rouletteGameService.endGame(CHANNEL_ID));
    }

    @Test
    void shot_returnsNotFoundMessageWhenNoActiveGame() {
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.empty());

        assertEquals("Game was not found =(", rouletteGameService.shot(CHANNEL_ID, USER_ID));
        commandHelper.verifyNoInteractions();
    }

    @Test
    void shot_returnsLossMessageWhenRandomIsZero() {
        RouletteGame game = new RouletteGame("desc", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(game));
        commandHelper.when(() -> CommandHelper.generateRandomNumber(6)).thenReturn(0);

        String result = rouletteGameService.shot(CHANNEL_ID, USER_ID);

        assertTrue(result.contains("lost the game"));
        assertTrue(result.contains("<@" + USER_ID + ">"));
        assertTrue(result.contains("desc"));
    }

    @Test
    void shot_returnsWinMessageWhenRandomIsNonZero() {
        RouletteGame game = new RouletteGame("desc", 6, CHANNEL_ID);
        when(rouletteGameRepo.findByChannelIdAndActive(CHANNEL_ID, true)).thenReturn(Optional.of(game));
        commandHelper.when(() -> CommandHelper.generateRandomNumber(6)).thenReturn(3);

        String result = rouletteGameService.shot(CHANNEL_ID, USER_ID);

        assertTrue(result.contains("was lucky today"));
        assertTrue(result.contains("<@" + USER_ID + ">"));
        assertTrue(result.contains("Game: desc"));
    }
}
