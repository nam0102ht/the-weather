package com.oddle.app.weather.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@Builder
public class RequestData {
    private long id;
    private String nameCity;
    private long timestamp;
    private JsonNode data;
}
