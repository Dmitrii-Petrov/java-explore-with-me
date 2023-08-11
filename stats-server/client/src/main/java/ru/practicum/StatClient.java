package ru.practicum;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Map;

@Component
public class StatClient {
    protected final RestTemplate rest;

    @Autowired
    public StatClient(RestTemplateBuilder builder) {
        this.rest = builder
                .uriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:9090"/*"http://stats-server:9090"*/))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                .build();
    }

    public ResponseEntity<StatDTO[]> getStatDTOs(Map<String, Object> parameters) {
        String urlTemplate = UriComponentsBuilder.fromHttpUrl("http://localhost:9090/stats")
                .queryParam("start", "{start}")
                .queryParam("end", "{end}")
                .queryParam("uris","{uris}")
                .queryParam("unique","{unique}")
                .encode()
                .toUriString();
        return rest.exchange(urlTemplate, HttpMethod.GET, null, StatDTO[].class,parameters);
    }

    public ResponseEntity<StatHitDTO> postHit(StatHitDTO statHitDTO) {
        return rest.exchange("/hit", HttpMethod.POST, new HttpEntity<>(statHitDTO), StatHitDTO.class);
    }
}
