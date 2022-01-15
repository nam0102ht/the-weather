package com.oddle.app.weather.api;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
@Builder
public class ResponseData {
    private int code;
    private boolean isStatus;
    private JsonNode message;
}
