package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.BanReason;

public interface BanReasonRepo extends JpaRepository<BanReason, Long> {
}
