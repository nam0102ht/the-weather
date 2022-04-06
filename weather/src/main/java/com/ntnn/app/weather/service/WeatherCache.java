package com.ntnn.app.weather.service;

import com.ntnn.app.weather.entity.WeatherData;
import com.ntnn.app.weather.repository.WeatherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class WeatherCache {
    private final WeatherRepository repository;

    @Cacheable(value = "weatherCache", key = "#from")
    public List<WeatherData> findAllWeather(long from, long to, int fromPage, int toPage) {
        Pageable pageable = PageRequest.of(fromPage, toPage);
        Page<WeatherData> pages = repository.findAllWeatherFrom(from, to, pageable);
        return pages.get().collect(Collectors.toList());
    }

    @CachePut(value = "weatherCache", key = "#weather.id")
    public WeatherData add(WeatherData weather) {
        return repository.save(weather);
    }

    @CachePut(value = "weatherCache", key = "#weather.id")
    public WeatherData update(WeatherData weather) {
        Optional<WeatherData> opt = repository.findById(weather.getId());
        if (!opt.isPresent()) return null;
        WeatherData weatherData = opt.get();
        weatherData.setNameCity(weather.getNameCity());
        weatherData.setTimestamp(weather.getTimestamp());
        weatherData.setData(weather.getData());
        return repository.save(weatherData);
    }


    @Cacheable(cacheNames = "weatherCache", key = "#id", unless = "#result == null")
    public WeatherData getWeatherById(long id) {
        return repository.findById(id).orElse(null);
    }

    @Caching(evict = {@CacheEvict(cacheNames = "weatherCache", key = "#weather.getId()"),
            @CacheEvict(cacheNames = "weatherCache", allEntries = true)})
    public void delete(WeatherData weather) {
        repository.delete(weather);
    }

}
