package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.User;

public interface UserRepo extends JpaRepository<User, Long> {
}
