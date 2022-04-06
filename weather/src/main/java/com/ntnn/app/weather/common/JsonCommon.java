package com.ntnn.app.weather.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JsonCommon {
    public static JsonNode convertStrToJson(String str) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(str);
        return actualObj;
    }
}
