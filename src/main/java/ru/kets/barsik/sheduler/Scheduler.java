package ru.kets.barsik.sheduler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.kets.barsik.configuration.JDABotConfiguration;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Scheduler {

    @Resource
    private JDABotConfiguration botConfiguration;

    @Scheduled(cron = "0 0 12 * * ?")
    public void cronJobSch() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("Java cron job expression:: " + strDate);

        JDA jda = botConfiguration.getJda();
        TextChannel textChannelById = jda.getTextChannelById("1041593203234717717");
        textChannelById.sendMessage("don't forget about me master!").queue();

    }
}
