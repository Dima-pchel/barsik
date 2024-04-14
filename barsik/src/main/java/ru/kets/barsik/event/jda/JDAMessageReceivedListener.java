package ru.kets.barsik.event.jda;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Service;

@Service
public class JDAMessageReceivedListener extends AbstractJDAMessageListener implements EventListener {

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        processCommand(event.getMessage(), event.getChannel());
    }

}
