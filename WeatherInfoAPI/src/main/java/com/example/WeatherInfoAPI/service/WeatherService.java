package com.example.WeatherInfoAPI.service;

import com.example.WeatherInfoAPI.model.PincodeLocation;
import com.example.WeatherInfoAPI.model.WeatherData;
import com.example.WeatherInfoAPI.repository.PincodeLocationRepository;
import com.example.WeatherInfoAPI.repository.WeatherDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.json.JSONObject;
import org.json.JSONArray;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class WeatherService {

    @Value("${google.maps.api.key}")
    private String googleMapsApiKey;

    @Value("${openweather.api.key}")
    private String openWeatherApiKey;

    @Autowired
    private PincodeLocationRepository locationRepository;

    @Autowired
    private WeatherDataRepository weatherDataRepository;

    private RestTemplate restTemplate = new RestTemplate();

    public WeatherData getWeatherInfo(String pincode, LocalDate date) {
        // Check if weather data is already in the cache
        Optional<WeatherData> cachedWeatherData = weatherDataRepository.findByPincodeAndDate(pincode, date);
        if (cachedWeatherData.isPresent()) {
            return cachedWeatherData.get();
        }

        // Get location data (latitude and longitude) for the pincode
        PincodeLocation location = getLocationByPincode(pincode);

        // Fetch weather information from OpenWeather API
        WeatherData weatherData = fetchWeatherData(location, date);
        weatherData.setPincode(pincode);

        // Save weather data to cache (database)
        return weatherDataRepository.save(weatherData);
    }

    private PincodeLocation getLocationByPincode(String pincode) {
        // Attempt to find the pincode location in the database
        return locationRepository.findById(pincode).orElseGet(() -> {
            // If not found, fetch the location using Google Maps API
            PincodeLocation location = fetchLatLngFromGoogleMaps(pincode);

            // Save the new location to the database for future calls
            locationRepository.save(location);

            return location;
        });
    }

    private PincodeLocation fetchLatLngFromGoogleMaps(String pincode) {
        String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" + pincode + "&key=" + googleMapsApiKey;
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray results = jsonResponse.getJSONArray("results");

        if (results.length() > 0) {
            JSONObject location = results.getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            double latitude = location.getDouble("lat");
            double longitude = location.getDouble("lng");
            return new PincodeLocation(pincode, latitude, longitude);
        } else {
            throw new RuntimeException("Unable to find location for pincode: " + pincode);
        }
    }

    private WeatherData fetchWeatherData(PincodeLocation location, LocalDate date) {
        String url = "https://api.openweathermap.org/data/2.5/onecall/timemachine?lat=" + location.getLatitude() +
                "&lon=" + location.getLongitude() + "&dt=" + date.atStartOfDay().toEpochSecond(ZoneOffset.UTC) +
                "&appid=" + openWeatherApiKey;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JSONObject jsonResponse = new JSONObject(response.getBody());
        JSONArray hourlyData = jsonResponse.getJSONArray("hourly");

        // Example: Take the first hourly data point for simplicity
        JSONObject weatherDetails = hourlyData.getJSONObject(0);
        double temperature = weatherDetails.getDouble("temp");
        int humidity = weatherDetails.getInt("humidity");
        String description = weatherDetails.getJSONArray("weather").getJSONObject(0).getString("description");

        return new WeatherData(date, temperature, humidity, description);
    }

}

