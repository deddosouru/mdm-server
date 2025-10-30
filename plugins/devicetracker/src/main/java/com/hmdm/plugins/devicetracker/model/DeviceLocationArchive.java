package com.hmdm.plugins.devicetracker.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;

@ApiModel(description = "Архивные данные местоположений устройства")
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceLocationArchive {
    @ApiModelProperty("ID записи")
    private Long id;
    
    @ApiModelProperty("ID устройства")
    private Long deviceId;
    
    @ApiModelProperty("Год")
    private Integer year;
    
    @ApiModelProperty("Месяц")
    private Integer month;
    
    @ApiModelProperty("Сжатые данные")
    private byte[] data;
    
    @ApiModelProperty("Флаг сжатия")
    private Boolean compressed = true;
    
    @ApiModelProperty("Количество точек")
    private Integer pointsCount;
    
    @ApiModelProperty("Дата создания")
    private Date createdAt;

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }
    
    public Integer getYear() { return year; }
    public void setYear(Integer year) { this.year = year; }
    
    public Integer getMonth() { return month; }
    public void setMonth(Integer month) { this.month = month; }
    
    public byte[] getData() { return data; }
    public void setData(byte[] data) { this.data = data; }
    
    public Boolean getCompressed() { return compressed; }
    public void setCompressed(Boolean compressed) { this.compressed = compressed; }
    
    public Integer getPointsCount() { return pointsCount; }
    public void setPointsCount(Integer pointsCount) { this.pointsCount = pointsCount; }
    
    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}