package ru.kets.barsik.service.impl;

import org.springframework.stereotype.Service;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.RouletteGameRepo;
import ru.kets.barsik.repo.pojo.RouletteGame;
import ru.kets.barsik.service.RouletteGameService;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class RouletteGameServiceImpl implements RouletteGameService {
    private static final String GAME_NOT_FOUND = "Game was not found =(";

    private static final Map<Integer, String> shotMap;

    static {
        Map<Integer, String> aMap = new HashMap<>();
        aMap.put(0, "https://cdn.discordapp.com/attachments/822912238519058462/1221607928121921547/1.png?ex=6613320a&is=6600bd0a&hm=b81ebc93a49f3d19a7d7822d6101bc65e825b906740467d1eeb79707100bbead&");
        aMap.put(1, "https://cdn.discordapp.com/attachments/822912238519058462/1221607928491147375/6.png?ex=6613320a&is=6600bd0a&hm=045efee9c5857fc636b7d890ad9c281c109b54e18d8ca62ce34ceded5ebe491a&");
        aMap.put(2, "https://cdn.discordapp.com/attachments/822912238519058462/1221607928860119131/5.png?ex=6613320a&is=6600bd0a&hm=78e2c898a5c511fa21cc2b6d4ff20d507e6710a1635abbcc268fe65c5876fbbd&");
        aMap.put(3, "https://cdn.discordapp.com/attachments/822912238519058462/1221607926444327013/4.png?ex=6613320a&is=6600bd0a&hm=09fa624fc475a2664f0f5e94b69f300ee3df8a016a37dee07bb5eb88577ea9b1&");
        aMap.put(4, "https://cdn.discordapp.com/attachments/822912238519058462/1221607926809100328/3.png?ex=6613320a&is=6600bd0a&hm=976665b8fd97e298f79fd202b788d36ea245b2d17f9359e79a0e6a6b933e6b91&");
        aMap.put(5, "https://cdn.discordapp.com/attachments/822912238519058462/1221607927241244814/2.png?ex=6613320a&is=6600bd0a&hm=97da9335fcb5e59cf3c3b9e40b97dd5f2906aca63ce352eb3c359267f5aade1f&");
        shotMap = Collections.unmodifiableMap(aMap);
    }

    @Resource
    private RouletteGameRepo rouletteGameRepo;

    @Override
    public String createNewGame(String description, String channelId) {
        rouletteGameRepo.findByChannelIdAndActive(channelId, true)
                .ifPresent(game -> {
                    game.setActive(false);
                    rouletteGameRepo.save(game);
                });
        RouletteGame newGame = new RouletteGame(description, 6, channelId);
        rouletteGameRepo.save(newGame);
        return newGame.toString();
    }

    @Override
    public String updateSlots(Integer shots, String channelId) {
        return rouletteGameRepo.findByChannelIdAndActive(channelId, true)
                .map(game -> modifySlots(game, shots))
                .orElse(GAME_NOT_FOUND);
    }

    @Override
    public String getGame(String channelId) {
        return rouletteGameRepo.findByChannelIdAndActive(channelId, true)
                .map(RouletteGame::toString)
                .orElse(GAME_NOT_FOUND);
    }

    @Override
    public String endGame(String channelId) {
        return rouletteGameRepo.findByChannelIdAndActive(channelId, true)
                .map(this::endGame)
                .orElse(GAME_NOT_FOUND);
    }

    @Override
    public String shot(String channelId, String userId) {
        return rouletteGameRepo.findByChannelIdAndActive(channelId, true)
                .map(game -> shot(game, userId))
                .orElse(GAME_NOT_FOUND);
    }

    private String shot(RouletteGame game, String userId) {
        int random = CommandHelper.generateRandomNumber(game.getShootsNumbers());
        if (random == 0) {
            return String.format("<@%s> lost the game.\n%s\n %s", userId, game.getDescription(), shotMap.get(random));
        } else {
            return String.format("Click..... <@%s> was lucky today. \n %s", userId, shotMap.get(random));
        }
    }

    private String endGame(RouletteGame game) {
        game.setActive(false);
        rouletteGameRepo.save(game);
        return "Game over";
    }

    private String modifySlots(RouletteGame game, Integer shots) {
        game.setShootsNumbers(shots <= 6 ? shots : 6);
        rouletteGameRepo.save(game);
        return game.toString();
    }
}
