package com.oddle.app.weather.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadRequestException extends Exception {
    private static final long serialVersionUID = -3387516993124229948L;
    private String message;
    private int codeError;

    public BadRequestException(int codeError, String message) {
        this.codeError = codeError;
        this.message = message;
    }
}
