package ru.kets.barsik.command;

import net.dv8tion.jda.api.entities.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.CatClient;

@Component("cat")
public class CatCommandHandler implements MessageCommandHandler {

    @Autowired
    CatClient catClient;

    @Override
    public String command(Message eventMessage) {
        return catClient.getImage();
    }
}
