package ru.kets.barsik.integrations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.kets.barsik.integrations.pojo.AnswerMessage;

import static ru.kets.barsik.constant.Constants.ERROR_MESSAGE;

@Component
public class AnswerClient extends AbstractImageClient implements ImageClient{

    Logger LOG = LoggerFactory.getLogger(AnswerClient.class);
    private static final String URL = "https://yesno.wtf/api";

    public String getImage() {
        try {
            AnswerMessage image = getImage(URL, AnswerMessage.class);
            StringBuilder sb = new StringBuilder(image.getAnswer());
            sb.append("\n").append(image.getImage());
            return sb.toString();
        } catch (Exception e) {
            LOG.error("We have a problem ", e);
            return ERROR_MESSAGE;
        }
    }
}
