package ru.kets.barsik.sheduler;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.apache.commons.lang3.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import ru.kets.barsik.configuration.JDABotConfiguration;
import ru.kets.barsik.repo.CronTaskRepo;
import ru.kets.barsik.repo.pojo.CronTask;

import java.util.Date;

@Component
public class MessageNotificationTaskManager {
    Logger LOG = LoggerFactory.getLogger(MessageNotificationTaskManager.class);

    private final ThreadPoolTaskScheduler executor;
    private final JDABotConfiguration botConfiguration;
    private final CronTaskRepo taskRepo;

    public MessageNotificationTaskManager(ThreadPoolTaskScheduler executor, JDABotConfiguration botConfiguration, CronTaskRepo taskRepo) {
        this.executor = executor;
        this.botConfiguration = botConfiguration;
        this.taskRepo = taskRepo;
    }

    //    "1 * * * * ?"
    public void execute(Long taskId, String cron) {
        RunnableTask runnable = new RunnableTask(taskId);
        executor.schedule(runnable, new CronTrigger(cron));
    }


    private class RunnableTask implements Runnable {
        private Long taskId;

        public RunnableTask(Long taskId) {
            this.taskId = taskId;
        }

        @Override
        public void run() {
            taskRepo.findById(taskId).ifPresent(this::processTask);
        }

        private void processTask(CronTask task) {
            JDA jda = botConfiguration.getJda();
            TextChannel textChannelById = jda.getTextChannelById(task.getChannelId());
            if (textChannelById != null && BooleanUtils.isTrue(task.getActive())) {
                textChannelById.sendMessage(task.getText()).queue();
                task.setLastUsage(new Date());
                taskRepo.save(task);
            } else {
                LOG.error("Channel with id {} not found", task.getChannelId());
            }
        }
    }

}
