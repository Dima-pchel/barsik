package ru.kets.barsik.sheduler.job;


import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kets.barsik.configuration.JDABotConfiguration;
import ru.kets.barsik.exception.EmbedCommandException;
import ru.kets.barsik.repo.SignupRepo;
import ru.kets.barsik.repo.pojo.Role;
import ru.kets.barsik.repo.pojo.Signup;
import ru.kets.barsik.service.SignupService;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class SignupNotificationCronJob {
    Logger LOG = LoggerFactory.getLogger(SignupNotificationCronJob.class);

    @Resource
    private JDABotConfiguration botConfiguration;

    @Resource
    private SignupRepo signupRepo;

    @Resource
    private SignupService signupService;


    @Scheduled(fixedRate = 300000L)
    public void checkSignups() {
        Date now = new Date();
        Date after = new Date(now.getTime() + (30 * 60 * 1000));
        JDA jda = botConfiguration.getJda();
        List<Signup> signups = ListUtils.emptyIfNull(signupRepo.findSignupsByDateBetweenAndNotificated(now, after, false));
        LOG.debug("signups found {}", signups.size());
        for (Signup signup : signups) {
            TextChannel textChannelById = jda.getTextChannelById(signup.getChannelId());
            if (textChannelById != null) {
                textChannelById.sendMessage(createNotifyMessage(signup)).queue();
                signup.setNotificated(true);
                signupRepo.save(signup);
                MessageEmbed embed = null;
                try {
                    embed = signupService.createEmbed(signup);
                } catch (EmbedCommandException e) {
                    LOG.error(e.getMessage());
                }
                textChannelById.sendMessageEmbeds(embed).queue();
            } else {
                LOG.error("channel with id {} not found", signup.getChannelId());
            }
        }

    }

    private String createNotifyMessage(Signup signup) {
        StringBuilder sb = new StringBuilder("Signup notification!!! \n");

        String name = StringUtils.isNotBlank(signup.getName()) ? signup.getName() : "Event";
        sb.append(name).append(" will start in the next 30 minutes").append("\n");


        Set<String> users = signup.getRoles()
                .stream()
                .filter(r -> StringUtils.isNotBlank(r.getUser()))
                .map(Role::getUser)
                .collect(Collectors.toSet());

        CollectionUtils.emptyIfNull(users).stream().forEach(u -> sb.append(" <@").append(u).append("> "));


        return sb.toString();
    }
}
