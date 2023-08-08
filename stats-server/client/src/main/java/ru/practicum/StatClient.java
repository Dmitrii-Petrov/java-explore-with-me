package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

@Component
public class StatClient {
    protected final RestTemplate rest;

    @Autowired
    public StatClient(RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://stats-server:9090"))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    protected ResponseEntity<StatDTO[]> getStatDTOs(Map<String, Object> parameters) {
        return rest.exchange("/stats", HttpMethod.GET, null, StatDTO[].class, parameters);
    }

    protected HttpStatus postHit(StatHitDTO statHitDTO) {
        return rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(statHitDTO), StatHitDTO.class).getStatusCode();
    }
}
