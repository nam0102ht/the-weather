package com.ntnn.app.weather.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class RequestSave {
    private long timestamp;
    private JsonNode data;
}
