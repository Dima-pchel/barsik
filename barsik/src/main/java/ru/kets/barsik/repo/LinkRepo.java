package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.Link;

import java.util.List;

public interface LinkRepo extends JpaRepository<Link, Long> {

    List<Link> findLinksByType(Link.Type type);
}
