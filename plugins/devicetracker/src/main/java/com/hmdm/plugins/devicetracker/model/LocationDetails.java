package com.hmdm.plugins.devicetracker.model;

import java.util.Date;

public class LocationDetails extends DeviceLocation {
    private Double calculatedSpeed; // Рассчитанная скорость между точками
    private Long stoppedTime; // Время стоянки в секундах
    private Double distance; // Расстояние от предыдущей точки в метрах
    
    public LocationDetails(DeviceLocation location) {
        setDeviceId(location.getDeviceId());
        setLat(location.getLat());
        setLon(location.getLon());
        setTimestamp(location.getTimestamp());
        setAccuracy(location.getAccuracy());
        setProvider(location.getProvider());
        setSpeed(location.getSpeed());
        setCourse(location.getCourse());
        setAltitude(location.getAltitude());
        setCustomerId(location.getCustomerId());
    }
    
    public Double getCalculatedSpeed() {
        return calculatedSpeed;
    }
    
    public void setCalculatedSpeed(Double calculatedSpeed) {
        this.calculatedSpeed = calculatedSpeed;
    }
    
    public Long getStoppedTime() {
        return stoppedTime;
    }
    
    public void setStoppedTime(Long stoppedTime) {
        this.stoppedTime = stoppedTime;
    }
    
    public Double getDistance() {
        return distance;
    }
    
    public void setDistance(Double distance) {
        this.distance = distance;
    }
}