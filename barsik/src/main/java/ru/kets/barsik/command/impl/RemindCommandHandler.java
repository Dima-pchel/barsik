package ru.kets.barsik.command.impl;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.MessageCommandHandler;
import ru.kets.barsik.helper.CommandHelper;
import ru.kets.barsik.repo.CronTaskRepo;
import ru.kets.barsik.repo.pojo.CronTask;
import ru.kets.barsik.sheduler.MessageNotificationTaskManager;

import javax.annotation.Resource;

import static ru.kets.barsik.constant.Constants.CommandName.REMIND_COMMAND_NAME;
import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component(REMIND_COMMAND_NAME)
public class RemindCommandHandler implements MessageCommandHandler {

    @Resource
    private CronTaskRepo taskRepo;
    @Resource
    private MessageNotificationTaskManager taskManager;


    //TODO later
    // barsik remind message {16:30 mon}
    // barsik remind message {16:30 *}
    @Override
    public String command(Message eventMessage) {
        return ERROR_MESSAGE;
//        MessageChannelUnion channel = eventMessage.getChannel();
//        String message = CommandHelper.extractMessage(eventMessage.getContentRaw(), REMIND_COMMAND_NAME);
//        message.substring(0, message.indexOf("{"));
//        CronTask task = new CronTask();
//        task.setActive(true);
//        task.setChannelId(channel.getId());
//        task.setText("test");
//        task.setCron("1 * * * * ?");
//
//        taskRepo.save(task);
//
//        taskManager.execute(task.getId(), task.getCron());
//        return "Success!";
    }

}
