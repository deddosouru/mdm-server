package com.hmdm.plugins.settings.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Date;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class SettingsTemplate {
    private Long id;
    private String name;
    private String description;
    private Long customerId;
    private List<TemplateSetting> settings;
    private boolean active;
    private Date createdAt;
    private Date updatedAt;

    // Целевое применение шаблона
    private List<Long> deviceIds;          // ID устройств
    private List<Long> groupIds;           // ID групп
    private List<Long> configurationIds;   // ID конфигураций
    private ApplyMode applyMode;           // Режим применения

    public enum ApplyMode {
        OVERRIDE,       // Заменить существующие настройки
        MERGE,         // Объединить с существующими настройками
        IF_NOT_SET     // Применить только если настройка не задана
    }

    // Геттеры и сеттеры
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public List<TemplateSetting> getSettings() { return settings; }
    public void setSettings(List<TemplateSetting> settings) { this.settings = settings; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    public List<Long> getDeviceIds() { return deviceIds; }
    public void setDeviceIds(List<Long> deviceIds) { this.deviceIds = deviceIds; }

    public List<Long> getGroupIds() { return groupIds; }
    public void setGroupIds(List<Long> groupIds) { this.groupIds = groupIds; }

    public List<Long> getConfigurationIds() { return configurationIds; }
    public void setConfigurationIds(List<Long> configurationIds) { this.configurationIds = configurationIds; }

    public ApplyMode getApplyMode() { return applyMode; }
    public void setApplyMode(ApplyMode applyMode) { this.applyMode = applyMode; }
}