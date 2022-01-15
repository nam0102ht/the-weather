package com.oddle.app.weather.repository;

import com.oddle.app.weather.entity.WeatherData;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WeatherRepository extends JpaRepository<WeatherData, Long> {
    @Query("SELECT u FROM Weathers u WHERE u.timestamp BETWEEN :from AND :to")
    Page<WeatherData> findAllWeatherFrom(@Param("from") long from, @Param("to") long to, Pageable pageable);
}
