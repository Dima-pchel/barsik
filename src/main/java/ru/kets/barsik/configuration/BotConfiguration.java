package ru.kets.barsik.configuration;

import discord4j.core.DiscordClientBuilder;
import discord4j.core.GatewayDiscordClient;
import discord4j.core.event.domain.Event;
import org.springframework.beans.factory.annotation.Value;
import ru.kets.barsik.event.d4j.EventListener;

import java.util.List;

@Deprecated
//TODO remove this
public class BotConfiguration {

    @Value("${token}")
    private String token;

    private GatewayDiscordClient client;

    public <T extends Event> GatewayDiscordClient gatewayDiscordClient(List<EventListener<T>> eventListeners) {
        GatewayDiscordClient client = getGatewayDiscordClient();

        for (EventListener<T> listener : eventListeners) {

            client.on(listener.getEventType())
                    .flatMap(listener::execute)
                    .onErrorResume(listener::handleError)
                    .subscribe();
        }

        return client;
    }

    public GatewayDiscordClient getGatewayDiscordClient() {
        if (this.client == null) {
            GatewayDiscordClient client = DiscordClientBuilder.create(token)
                    .build()
                    .login()
                    .block();
            this.client = client;
            return client;
        }
        return client;
    }


}
