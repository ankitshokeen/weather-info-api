package com.example.WeatherInfoAPI.model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class PincodeLocation {
    @Id
    private String pincode;
    private Double latitude;
    private Double longitude;

    public PincodeLocation(String pincode, double latitude, double longitude) {
    }


    // Getters and Setters


    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
