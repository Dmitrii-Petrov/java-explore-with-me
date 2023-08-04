package ru.practicum;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;
import java.util.Map;

@Service
public class StatClient extends BaseClient {


    private static final String API_PREFIX = "";

    @Autowired
    public StatClient(String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(HttpComponentsClientHttpRequestFactory::new)
                        .build()
        );
    }

    public ResponseEntity<Object> postHit(StatHitDTO statHitDTO) {
        return post(statHitDTO);
    }

    public ResponseEntity<Object> getStat(String start, String end, List<String> uris, Boolean unique) {
        Map<String, Object> parameters = Map.of("start", start, "end", end, "unique", unique);
        StringBuilder urisString = new StringBuilder();
        for (String uri : uris) {
            urisString.append("&uris=").append(uri);
        }
        return get("/stats?start={start}&end={end}" + urisString + "&unique={unique}", parameters);
    }
}
