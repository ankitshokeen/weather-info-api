package com.example.WeatherInfoAPI.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class WeatherData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pincode;
    private LocalDate date;
    private String temperature;
    private String humidity;
    private String weatherDescription;

    @ManyToOne
    @JoinColumn(name = "pincode", referencedColumnName = "pincode", insertable = false, updatable = false)
    private PincodeLocation location;

    public WeatherData(LocalDate date, double temperature, int humidity, String description) {
    }

    // Getters and Setters


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public PincodeLocation getLocation() {
        return location;
    }

    public void setLocation(PincodeLocation location) {
        this.location = location;
    }
}
