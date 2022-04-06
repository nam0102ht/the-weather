package com.ntnn.app.weather.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity(name = "Weathers")
@Setter
@Getter
@Data
@Table(name = "Weathers")
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WeatherData implements Serializable {
    private static final long serialVersionUID = -3387516993124229948L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String nameCity;

    private long timestamp;

    @Column(columnDefinition = "TEXT")
    private String data;
}
