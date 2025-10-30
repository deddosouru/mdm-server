package com.hmdm.plugins.settings.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.persistence.DeviceDAO;
import com.hmdm.plugins.settings.model.DeviceConfig;
import com.hmdm.plugins.settings.model.ConfigTemplate;
import com.hmdm.plugins.settings.persistence.SettingsDAO;
import com.hmdm.security.SecurityContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Date;

@Singleton
public class SettingsService {
    
    private static final Logger log = LoggerFactory.getLogger(SettingsService.class);
    
    private final SettingsDAO settingsDAO;
    private final DeviceDAO deviceDAO;
    
    // Кэш для отслеживания отправленных конфигураций
    private final Map<Long, Date> deviceConfigCache = new ConcurrentHashMap<>();
    
    @Inject
    public SettingsService(SettingsDAO settingsDAO, DeviceDAO deviceDAO) {
        this.settingsDAO = settingsDAO;
        this.deviceDAO = deviceDAO;
    }

    /**
     * Типы поддерживаемых настроек
     */
    public enum ConfigType {
        WIFI("WiFi Settings"),
        BLUETOOTH("Bluetooth Settings"),
        DISPLAY("Display Settings"),
        SOUND("Sound Settings"),
        SECURITY("Security Settings"),
        POWER("Power Management"),
        APP("Application Settings"),
        SYSTEM("System Settings");

        private final String description;

        ConfigType(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }
    }

    /**
     * Получает список доступных типов настроек
     */
    public List<Map<String, String>> getAvailableConfigTypes() {
        List<Map<String, String>> types = new ArrayList<>();
        for (ConfigType type : ConfigType.values()) {
            Map<String, String> typeInfo = new HashMap<>();
            typeInfo.put("id", type.name());
            typeInfo.put("description", type.getDescription());
            types.add(typeInfo);
        }
        return types;
    }

    /**
     * Применяет шаблон к устройству
     */
    public void applyTemplate(Long deviceId, Long templateId) {
        ConfigTemplate template = settingsDAO.getTemplate(templateId);
        if (template == null) {
            throw new IllegalArgumentException("Template not found: " + templateId);
        }

        // Создаем конфигурацию из шаблона
        DeviceConfig config = new DeviceConfig();
        config.setDeviceId(deviceId);
        config.setConfigType(template.getConfigType());
        config.setConfigValue(template.getConfigData());
        config.setCustomerId(SecurityContext.get().getCurrentUser().getCustomerId());
        
        settingsDAO.saveConfig(config);
    }

    /**
     * Получает ожидающие применения настройки для устройства
     */
    public List<DeviceConfig> getPendingConfigs(Long deviceId) {
        // Проверяем, не слишком ли часто запрашиваются настройки
        Date lastCheck = deviceConfigCache.get(deviceId);
        Date now = new Date();
        
        if (lastCheck != null && (now.getTime() - lastCheck.getTime() < 60000)) {
            // Если прошло меньше минуты, пропускаем
            return List.of();
        }
        
        // Обновляем время последней проверки
        deviceConfigCache.put(deviceId, now);
        
        List<DeviceConfig> configs = settingsDAO.getPendingConfigs(deviceId);
        
        // Помечаем конфигурации как отправленные
        for (DeviceConfig config : configs) {
            settingsDAO.updateConfigStatus(
                config.getId(), 
                DeviceConfig.ConfigStatus.SENT,
                null
            );
        }
        
        return configs;
    }

    /**
     * Обновляет статус применения настройки
     */
    public void updateConfigStatus(Long configId, boolean success, String errorMessage) {
        DeviceConfig.ConfigStatus status = success ? 
            DeviceConfig.ConfigStatus.APPLIED : 
            DeviceConfig.ConfigStatus.FAILED;
            
        settingsDAO.updateConfigStatus(configId, status, errorMessage);
    }

    /**
     * Получает историю настроек устройства
     */
    public List<DeviceConfig> getDeviceConfigHistory(Long deviceId) {
        return settingsDAO.getDeviceConfigs(deviceId);
    }
}