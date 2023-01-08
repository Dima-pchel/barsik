package ru.kets.barsik.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.pojo.CatMessage;

import static ru.kets.barsik.integrations.constant.Constants.ERROR_MESSAGE;

@Component
public class CatClient extends AbstractImageClient implements ImageClient{
    Logger LOG = LoggerFactory.getLogger(CatClient.class);
    private static final String CAT_URL = "https://aws.random.cat/meow";

    public String getImage() {
        try {
            CatMessage image = getImage(CAT_URL, CatMessage.class);
            return image.getFile();
        } catch (Exception e) {
            LOG.error("We have a problem ", e);
            return ERROR_MESSAGE;
        }
    }


}
