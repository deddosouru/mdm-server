package com.hmdm.plugins.devicetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(description = "Настройки плагина отслеживания устройств")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceTrackerSettings {
    @ApiModelProperty("ID клиента")
    private Long customerId;
    
    @ApiModelProperty("Интервал обновления в секундах")
    private Integer updateInterval = 300;
    
    @ApiModelProperty("Количество дней хранения истории")
    private Integer retentionDays = 30;
    
    @ApiModelProperty("Минимальная точность в метрах")
    private Double minAccuracy = 100.0;
    
    @ApiModelProperty("Минимальное расстояние между точками в метрах")
    private Double minDistance = 50.0;

    // Геттеры и сеттеры
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    public Integer getUpdateInterval() { return updateInterval; }
    public void setUpdateInterval(Integer updateInterval) { this.updateInterval = updateInterval; }
    
    public Integer getRetentionDays() { return retentionDays; }
    public void setRetentionDays(Integer retentionDays) { this.retentionDays = retentionDays; }
    
    public Double getMinAccuracy() { return minAccuracy; }
    public void setMinAccuracy(Double minAccuracy) { this.minAccuracy = minAccuracy; }
    
    public Double getMinDistance() { return minDistance; }
    public void setMinDistance(Double minDistance) { this.minDistance = minDistance; }
}