package com.ntnn.app.weather.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.ntnn.app.weather.common.JsonCommon;
import com.ntnn.app.weather.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Slf4j
@Service
public class CallApiWeather {

    @Value("${downStream.url}")
    private String downStreamUrl;

    @Value("${downStream.apiKey}")
    private String apiKey;

    public JsonNode callApi(String city) throws IOException, BadRequestException {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String key = new String(Base64.getDecoder().decode(apiKey), StandardCharsets.UTF_8);
            StringBuffer buffer = new StringBuffer();
            buffer.append(downStreamUrl).append("?q=").append(city).append("&appid=").append(key);
            log.info("downStreamUrl: {}", buffer);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Object> entity = new HttpEntity<Object>(headers);

            ResponseEntity<String> responseTemplate = restTemplate.exchange(buffer.toString(), HttpMethod.GET, entity, String.class);
            return JsonCommon.convertStrToJson(responseTemplate.getBody());
        } catch (HttpClientErrorException | HttpServerErrorException httpClientOrServerExc) {
            if(HttpStatus.NOT_FOUND.equals(httpClientOrServerExc.getStatusCode())) {
                // your handling of "NOT FOUND" here
                // e.g. throw new RuntimeException("Your Error Message here", httpClientOrServerExc);
                return JsonCommon.convertStrToJson(httpClientOrServerExc.getResponseBodyAsString());
            } else {
                throw new BadRequestException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unknow Error Occur");
            }
        }
    }
}
