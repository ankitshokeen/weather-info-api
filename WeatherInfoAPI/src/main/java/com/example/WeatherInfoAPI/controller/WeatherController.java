package com.example.WeatherInfoAPI.controller;

import com.example.WeatherInfoAPI.model.WeatherData;
import com.example.WeatherInfoAPI.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    @GetMapping
    public WeatherData getWeather(
            @RequestParam String pincode,
            @RequestParam String for_date) {
        LocalDate date = LocalDate.parse(for_date);
        return weatherService.getWeatherInfo(pincode, date);
    }
}
