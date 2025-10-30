package com.hmdm.plugins.settings.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.persistence.AbstractDAO;
import com.hmdm.plugins.settings.model.DeviceConfig;
import com.hmdm.plugins.settings.model.ConfigTemplate;
import com.hmdm.security.SecurityContext;
import org.mybatis.guice.transactional.Transactional;

import java.util.Date;
import java.util.List;

@Singleton
public class SettingsDAO extends AbstractDAO<DeviceConfig> {

    @Inject
    public SettingsDAO() {
        super(DeviceConfig.class);
    }

    /**
     * Получает список конфигураций для устройства
     */
    public List<DeviceConfig> getDeviceConfigs(Long deviceId) {
        return getSecureSqlSession().selectList(
            "SettingsMapper.getDeviceConfigs",
            deviceId
        );
    }

    /**
     * Получает список ожидающих применения конфигураций
     */
    public List<DeviceConfig> getPendingConfigs(Long deviceId) {
        return getSecureSqlSession().selectList(
            "SettingsMapper.getPendingConfigs",
            deviceId
        );
    }

    /**
     * Сохраняет новую конфигурацию
     */
    @Transactional
    public void saveConfig(DeviceConfig config) {
        if (config.getId() == null) {
            config.setCreatedAt(new Date());
            config.setStatus(DeviceConfig.ConfigStatus.PENDING);
            getSecureSqlSession().insert("SettingsMapper.insertConfig", config);
        } else {
            config.setUpdatedAt(new Date());
            getSecureSqlSession().update("SettingsMapper.updateConfig", config);
        }
    }

    /**
     * Обновляет статус конфигурации
     */
    @Transactional
    public void updateConfigStatus(Long configId, DeviceConfig.ConfigStatus status, String errorMessage) {
        DeviceConfig update = new DeviceConfig();
        update.setId(configId);
        update.setStatus(status);
        update.setErrorMessage(errorMessage);
        update.setUpdatedAt(new Date());
        
        if (status == DeviceConfig.ConfigStatus.APPLIED) {
            update.setAppliedAt(new Date());
        }
        
        getSecureSqlSession().update("SettingsMapper.updateConfigStatus", update);
    }

    /**
     * Получает список шаблонов
     */
    public List<ConfigTemplate> getTemplates() {
        return getSecureSqlSession().selectList("SettingsMapper.getTemplates");
    }

    /**
     * Сохраняет шаблон
     */
    @Transactional
    public void saveTemplate(ConfigTemplate template) {
        if (template.getId() == null) {
            template.setCreatedAt(new Date());
            getSecureSqlSession().insert("SettingsMapper.insertTemplate", template);
        } else {
            template.setUpdatedAt(new Date());
            getSecureSqlSession().update("SettingsMapper.updateTemplate", template);
        }
    }

    /**
     * Удаляет шаблон
     */
    @Transactional
    public void deleteTemplate(Long templateId) {
        getSecureSqlSession().delete("SettingsMapper.deleteTemplate", templateId);
    }
}