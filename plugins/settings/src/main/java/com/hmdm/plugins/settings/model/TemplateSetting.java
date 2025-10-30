package com.hmdm.plugins.settings.model;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateSetting {
    private SettingType type;
    private String key;
    private String value;
    private String description;
    private boolean mandatory;
    private ValidationRule validationRule;

    public enum ValidationRule {
        NONE,
        NUMERIC,
        BOOLEAN,
        EMAIL,
        IP_ADDRESS,
        URL,
        REGEX
    }

    private String validationPattern;  // Для REGEX правила
    private String validationMessage;  // Сообщение об ошибке валидации

    // Геттеры и сеттеры
    public SettingType getType() { return type; }
    public void setType(SettingType type) { this.type = type; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }

    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isMandatory() { return mandatory; }
    public void setMandatory(boolean mandatory) { this.mandatory = mandatory; }

    public ValidationRule getValidationRule() { return validationRule; }
    public void setValidationRule(ValidationRule validationRule) { this.validationRule = validationRule; }

    public String getValidationPattern() { return validationPattern; }
    public void setValidationPattern(String validationPattern) { this.validationPattern = validationPattern; }

    public String getValidationMessage() { return validationMessage; }
    public void setValidationMessage(String validationMessage) { this.validationMessage = validationMessage; }
}