package com.ntnn.app.weather.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ntnn.app.weather.api.RequestData;
import com.ntnn.app.weather.api.RequestHistory;
import com.ntnn.app.weather.api.RequestSave;
import com.ntnn.app.weather.api.ResponseData;
import com.ntnn.app.weather.entity.WeatherData;
import com.ntnn.app.weather.exception.BadRequestException;
import com.ntnn.app.weather.service.CallApiWeather;
import com.ntnn.app.weather.service.WeatherCache;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@AllArgsConstructor
public class WeatherController {
    private final CallApiWeather callApiWeather;
    private final WeatherCache weatherCache;

    @GetMapping("")
    public Map<String, Object> getWeathers() {
        return Collections.singletonMap("message", "Welcome to Oddle Backend Challenge");
    }

    @GetMapping("/weather")
    public ResponseEntity<ResponseData> getWeathersByCity(@RequestParam(value = "city") String city) throws BadRequestException {
        log.info("GET method call current weather");
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        JsonNode dataRsp = null;
        try {
            log.info("Calling API");
            dataRsp = callApiWeather.callApi(city);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        objectNode.put("status", "Success");
        objectNode.put("data", dataRsp);
        return new ResponseEntity<>(ResponseData.builder()
                .code(200)
                .isStatus(true)
                .message(objectNode.put("status", "Success"))
                .build(), HttpStatus.OK);
    }

    @PostMapping("/weather")
    public ResponseEntity<ResponseData> saveWeathers(@RequestBody RequestSave requestSave) {
        log.info("Api is called to save weather");
        WeatherData weatherData = new WeatherData();
        weatherData.setTimestamp(requestSave.getTimestamp());
        weatherData.setNameCity(requestSave.getData().get("name").asText());
        weatherData.setData(requestSave.getData().toString());
        WeatherData weather = weatherCache.add(weatherData);
        if (weather == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.convertValue(weather, JsonNode.class);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("status", "Success");
        objectNode.put("data", jsonNode);
        ResponseData responseData = ResponseData.builder()
                .code(200)
                .isStatus(true)
                .message(objectNode)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @PutMapping("/weather")
    public ResponseEntity<ResponseData> updateWeather(@RequestBody RequestData requestSave) {
        log.info("Api is called to save weather");
        ObjectMapper objectMapper = new ObjectMapper();
        if (requestSave.getId() == 0) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("status", "Id not equal 0");
            ResponseData responseData = ResponseData.builder()
                    .code(400)
                    .isStatus(false)
                    .message(objectNode)
                    .build();
            return new ResponseEntity<>(responseData, HttpStatus.OK);
        }
        WeatherData weatherData = new WeatherData();
        weatherData.setTimestamp(requestSave.getTimestamp());
        weatherData.setId(requestSave.getId());
        weatherData.setNameCity(requestSave.getNameCity());
        weatherData.setData(requestSave.getData().toString());
        WeatherData weather = weatherCache.update(weatherData);
        if (weather == null) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        JsonNode jsonNode = objectMapper.convertValue(weather, JsonNode.class);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("status", "Success");
        objectNode.put("data", jsonNode);
        ResponseData responseData = ResponseData.builder()
                .code(200)
                .isStatus(true)
                .message(objectNode)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @GetMapping("/weather/histories")
    public ResponseEntity<ResponseData> getWeatherHistories(@RequestBody RequestHistory requestHistory) {
        log.info("Get all weather histories");
        List<WeatherData> lst = weatherCache.findAllWeather(requestHistory.getFrom(),
                requestHistory.getTo(),
                requestHistory.getFromPage(),
                requestHistory.getToPage());

        ObjectMapper objectMapper = new ObjectMapper();
        ArrayNode jsonNode = objectMapper.convertValue(lst, ArrayNode.class);
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("status", "Success");
        objectNode.put("data", jsonNode);
        ResponseData responseData = ResponseData.builder()
                .code(200)
                .isStatus(true)
                .message(objectNode)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }

    @DeleteMapping("/weather")
    public ResponseEntity<ResponseData> deleteWeather(@RequestParam long id) {
        log.info("Delete the weather of");

        WeatherData data = weatherCache.getWeatherById(id);

        if (data == null) {
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode objectNode = objectMapper.createObjectNode();
            objectNode.put("status", "Fail");
            objectNode.put("message", "Weather ID not found");
            return new ResponseEntity<>(ResponseData.builder()
                    .code(404)
                    .isStatus(true)
                    .message(objectNode)
                    .build(), HttpStatus.OK);
        }
        WeatherData weatherData = new WeatherData();
        weatherData.setTimestamp(data.getTimestamp());
        weatherData.setId(id);
        weatherData.setNameCity(data.getNameCity());
        weatherData.setData(data.getData().toString());
        weatherCache.delete(weatherData);

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("status", "Success");
        objectNode.put("message", "Weather of id: " + id + " is deleted");
        ResponseData responseData = ResponseData.builder()
                .code(200)
                .isStatus(true)
                .message(objectNode)
                .build();
        return new ResponseEntity<>(responseData, HttpStatus.OK);
    }
}