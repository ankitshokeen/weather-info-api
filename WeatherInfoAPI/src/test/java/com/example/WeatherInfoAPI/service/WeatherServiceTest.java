package com.example.WeatherInfoAPI.service;

import com.example.WeatherInfoAPI.model.WeatherData;
import com.example.WeatherInfoAPI.repository.WeatherDataRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

@SpringBootTest
public class WeatherServiceTest {

    @Autowired
    private WeatherService weatherService;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    @Test
    public void testWeatherCaching() {
        WeatherData weather1 = weatherService.getWeatherInfo("411014", LocalDate.parse("2020-10-15"));
        WeatherData weather2 = weatherService.getWeatherInfo("411014", LocalDate.parse("2020-10-15"));

        Mockito.verify(weatherDataRepository, Mockito.times(1)).findByPincodeAndDate("411014", LocalDate.parse("2020-10-15"));
    }
}
