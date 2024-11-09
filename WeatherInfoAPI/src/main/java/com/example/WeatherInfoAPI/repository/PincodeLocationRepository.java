package com.example.WeatherInfoAPI.repository;

import com.example.WeatherInfoAPI.model.PincodeLocation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PincodeLocationRepository extends JpaRepository<PincodeLocation, String> {}
