package com.hmdm.plugins.settings.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hmdm.rest.json.LookupItem;
import io.swagger.annotations.ApiModel;

import java.io.Serializable;
import java.util.Date;

@ApiModel(description = "Шаблон настроек")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConfigTemplate implements Serializable, LookupItem {
    
    private Long id;
    private Long customerId;
    private String name;
    private String description;
    private String configType;
    private String configData;
    private Date createdAt;
    private Date updatedAt;

    // Геттеры и сеттеры
    @Override
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getConfigType() { return configType; }
    public void setConfigType(String configType) { this.configType = configType; }

    public String getConfigData() { return configData; }
    public void setConfigData(String configData) { this.configData = configData; }

    public Date getCreatedAt() { return createdAt; }
    public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }

    public Date getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Date updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String getValue() {
        return name;
    }
}