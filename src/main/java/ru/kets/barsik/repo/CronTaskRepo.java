package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.CronTask;

public interface CronTaskRepo extends JpaRepository<CronTask, Long> {
}
