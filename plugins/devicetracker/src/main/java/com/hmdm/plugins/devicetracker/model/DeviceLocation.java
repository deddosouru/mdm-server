package com.hmdm.plugins.devicetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel(description = "Представляет местоположение устройства")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceLocation {
    @ApiModelProperty("ID устройства")
    private Long deviceId;
    
    @ApiModelProperty("Широта")
    private Double latitude;
    
    @ApiModelProperty("Долгота")
    private Double longitude;
    
    @ApiModelProperty("Точность в метрах")
    private Double accuracy;
    
    @ApiModelProperty("Скорость в м/с")
    private Double speed;
    
    @ApiModelProperty("Уровень заряда батареи в процентах")
    private Integer battery;
    
    @ApiModelProperty("Временная метка")
    private Date timestamp;
    
    @ApiModelProperty("Адрес местоположения")
    private String address;

    // Геттеры и сеттеры
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    
    public Double getLatitude() { return latitude; }
    public void setLatitude(Double latitude) { this.latitude = latitude; }
    
    public Double getLongitude() { return longitude; }
    public void setLongitude(Double longitude) { this.longitude = longitude; }
    
    public Double getAccuracy() { return accuracy; }
    public void setAccuracy(Double accuracy) { this.accuracy = accuracy; }
    
    public Double getSpeed() { return speed; }
    public void setSpeed(Double speed) { this.speed = speed; }
    
    public Integer getBattery() { return battery; }
    public void setBattery(Integer battery) { this.battery = battery; }
    
    public Date getTimestamp() { return timestamp; }
    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}