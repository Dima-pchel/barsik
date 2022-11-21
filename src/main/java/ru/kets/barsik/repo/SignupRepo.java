package ru.kets.barsik.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kets.barsik.repo.pojo.Signup;

import java.util.List;

public interface SignupRepo extends JpaRepository<Signup, Long> {

    Signup findSignupByChannelId(String channelId);

    Signup findSignupByNameAndChannelId(String name, String channelId);

    Signup findSignupByName(String name);

    List<Signup> findSignupsByChannelIdAndActive(String channelId, Boolean active);
}
