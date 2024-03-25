package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.RouletteGame;

import java.util.Optional;

public interface RouletteGameRepo extends JpaRepository<RouletteGame, Long> {

    Optional<RouletteGame> findByChannelIdAndActive(String aLong, boolean active);

}
