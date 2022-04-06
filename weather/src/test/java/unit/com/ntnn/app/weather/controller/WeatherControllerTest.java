package unit.com.ntnn.app.weather.controller;

import com.ntnn.app.weather.controller.WeatherController;
import com.ntnn.app.weather.service.CallApiWeather;
import com.ntnn.app.weather.service.WeatherCache;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class WeatherControllerTest {
    @InjectMocks
    private WeatherController weatherController;

    @Mock
    private CallApiWeather callApiWeather;

    @Mock
    private WeatherCache weatherCache;

    @Test
    public void getWeathers() {
        Assertions.assertDoesNotThrow(() -> {
            weatherController.getWeathers();
        });
    }
}
