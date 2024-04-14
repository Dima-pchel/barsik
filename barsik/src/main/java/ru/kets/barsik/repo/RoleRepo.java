package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.Role;

public interface RoleRepo extends JpaRepository<Role, Long> {
}
