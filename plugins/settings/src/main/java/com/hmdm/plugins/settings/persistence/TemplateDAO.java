package com.hmdm.plugins.settings.persistence;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.persistence.AbstractDAO;
import com.hmdm.plugins.settings.model.SettingsTemplate;
import com.hmdm.security.SecurityContext;
import org.mybatis.guice.transactional.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Set;

@Singleton
public class TemplateDAO extends AbstractDAO<SettingsTemplate> {

    @Inject
    public TemplateDAO() {
        super(SettingsTemplate.class);
    }

    /**
     * Получает список всех шаблонов
     */
    public List<SettingsTemplate> getAllTemplates() {
        return getSecureSqlSession().selectList("TemplateMapper.getAllTemplates");
    }

    /**
     * Получает шаблон по ID
     */
    public SettingsTemplate getTemplate(Long id) {
        return getSecureSqlSession().selectOne("TemplateMapper.getTemplate", id);
    }

    /**
     * Сохраняет шаблон
     */
    @Transactional
    public void saveTemplate(SettingsTemplate template) {
        Date now = new Date();
        if (template.getId() == null) {
            template.setCreatedAt(now);
            template.setCustomerId(SecurityContext.get().getCurrentUser().getCustomerId());
            getSecureSqlSession().insert("TemplateMapper.insertTemplate", template);
        } else {
            template.setUpdatedAt(now);
            getSecureSqlSession().update("TemplateMapper.updateTemplate", template);
        }

        // Обновляем связи
        updateTemplateLinks(template);
    }

    /**
     * Удаляет шаблон
     */
    @Transactional
    public void deleteTemplate(Long id) {
        getSecureSqlSession().delete("TemplateMapper.deleteTemplate", id);
    }

    /**
     * Получает шаблоны для устройства
     */
    public List<SettingsTemplate> getTemplatesForDevice(Long deviceId) {
        return getSecureSqlSession().selectList("TemplateMapper.getTemplatesForDevice", deviceId);
    }

    /**
     * Получает шаблоны для группы
     */
    public List<SettingsTemplate> getTemplatesForGroup(Long groupId) {
        return getSecureSqlSession().selectList("TemplateMapper.getTemplatesForGroup", groupId);
    }

    /**
     * Получает шаблоны для конфигурации
     */
    public List<SettingsTemplate> getTemplatesForConfiguration(Long configId) {
        return getSecureSqlSession().selectList("TemplateMapper.getTemplatesForConfiguration", configId);
    }

    /**
     * Обновляет связи шаблона
     */
    @Transactional
    private void updateTemplateLinks(SettingsTemplate template) {
        Long templateId = template.getId();
        
        // Обновляем связи с устройствами
        getSecureSqlSession().delete("TemplateMapper.deleteDeviceLinks", templateId);
        if (template.getDeviceIds() != null && !template.getDeviceIds().isEmpty()) {
            getSecureSqlSession().insert("TemplateMapper.insertDeviceLinks", 
                new TemplateLinkBatch(templateId, template.getDeviceIds()));
        }
        
        // Обновляем связи с группами
        getSecureSqlSession().delete("TemplateMapper.deleteGroupLinks", templateId);
        if (template.getGroupIds() != null && !template.getGroupIds().isEmpty()) {
            getSecureSqlSession().insert("TemplateMapper.insertGroupLinks",
                new TemplateLinkBatch(templateId, template.getGroupIds()));
        }
        
        // Обновляем связи с конфигурациями
        getSecureSqlSession().delete("TemplateMapper.deleteConfigurationLinks", templateId);
        if (template.getConfigurationIds() != null && !template.getConfigurationIds().isEmpty()) {
            getSecureSqlSession().insert("TemplateMapper.insertConfigurationLinks",
                new TemplateLinkBatch(templateId, template.getConfigurationIds()));
        }
    }

    private static class TemplateLinkBatch {
        private final Long templateId;
        private final List<Long> targetIds;
        private final Date createdAt;

        public TemplateLinkBatch(Long templateId, List<Long> targetIds) {
            this.templateId = templateId;
            this.targetIds = targetIds;
            this.createdAt = new Date();
        }

        public Long getTemplateId() { return templateId; }
        public List<Long> getTargetIds() { return targetIds; }
        public Date getCreatedAt() { return createdAt; }
    }
}