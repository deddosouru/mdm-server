package com.hmdm.plugins.settings.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hmdm.rest.json.LookupItem;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "Конфигурация настроек устройства")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DeviceConfig implements Serializable, LookupItem {
    
    private Long id;
    private Long deviceId;
    private Long customerId;
    private String configType;
    private String configKey;
    private String configValue;
    private ConfigStatus status;
    private Date createdAt;
    private Date updatedAt;
    private Date appliedAt;
    private String errorMessage;

    public enum ConfigStatus {
        PENDING,    // Ожидает применения
        SENT,       // Отправлено на устройство
        APPLIED,    // Успешно применено
        FAILED      // Ошибка применения
    }

    // Геттеры и сеттеры
    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getDeviceId() { return deviceId; }
    public void setDeviceId(Long deviceId) { this.deviceId = deviceId; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }

    public String getConfigKey() { return configKey; }
    public void setConfigKey(String configKey) { this.configKey = configKey; }

    public String getConfigValue() { return configValue; }
    public void setConfigValue(String configValue) { this.configValue = configValue; }

    public ConfigStatus getStatus() { return status; }
    public void setStatus(ConfigStatus status) { this.status = status; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public Date getAppliedAt() { return appliedAt; }
    public void setAppliedAt(Date appliedAt) { this.appliedAt = appliedAt; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    @Override
    public String getValue() {
        return configKey + ": " + configValue;
    }
}