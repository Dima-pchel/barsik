package ru.kets.barsik.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.pojo.DogMessage;

import static ru.kets.barsik.Constants.ERROR_MESSAGE;

@Component()
public class DogClient extends AbstractImageClient implements ImageClient {
    Logger LOG = LoggerFactory.getLogger(CatClient.class);
    private static final String GOG_URL = "https://random.dog/woof.json";

    public String getImage() {
        try {
            DogMessage image = getImage(GOG_URL, DogMessage.class);
            return image.getUrl();
        } catch (Exception e) {
            LOG.error("We have a problem ", e);
            return ERROR_MESSAGE;
        }
    }
}
