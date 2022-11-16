package ru.kets.barsik.event.jda;

import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.hooks.EventListener;
import org.springframework.stereotype.Service;

@Service
public class JDAMessageUpdateListener extends AbstractJDAMessageListener implements EventListener {

    @Override
    public void onMessageUpdate(MessageUpdateEvent event) {
        processCommand(event.getMessage(), event.getChannel());
    }

}
