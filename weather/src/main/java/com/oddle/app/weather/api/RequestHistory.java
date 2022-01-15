package com.oddle.app.weather.api;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class RequestHistory {
    private long from;
    private long to;
    private int fromPage;
    private int toPage;
}
