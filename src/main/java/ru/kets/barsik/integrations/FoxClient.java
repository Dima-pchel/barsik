package ru.kets.barsik.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.command.AvatarCommandHandler;
import ru.kets.barsik.integrations.pojo.FoxMessage;

import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component
public class FoxClient extends AbstractImageClient implements ImageClient {
    Logger LOG = LoggerFactory.getLogger(FoxClient.class);
    private static final String FOX_URL = "https://randomfox.ca/floof/";

    public String getImage() {
        try {
            FoxMessage image = getImage(FOX_URL, FoxMessage.class);
            return image.getImage();
        } catch (Exception e) {
            LOG.error("We have a problem ", e);
            return ERROR_MESSAGE;
        }
    }
}
