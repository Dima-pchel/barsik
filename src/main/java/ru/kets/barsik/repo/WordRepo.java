package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.Word;

import java.util.List;

public interface WordRepo extends JpaRepository<Word, Long> {

    List<Word> findWordsByType(Word.Type t);
}
