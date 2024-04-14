package ru.kets.barsik.integrations;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kets.barsik.integrations.pojo.CatMessage;

import java.net.URI;

public abstract class AbstractImageClient {

    <T> T getImage(String url, Class<T> tClass) {
        RestTemplate restTemplate = new RestTemplate();
        URI uri = getURI(url);
        T message = restTemplate.getForObject(uri, tClass);
        return message;
    }

    URI getURI(String host) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(host);
        return builder.build().encode().toUri();
    }
}
