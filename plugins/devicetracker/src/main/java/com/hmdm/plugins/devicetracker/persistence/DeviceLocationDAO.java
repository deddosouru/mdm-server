package com.hmdm.plugins.devicetracker.persistence;

import com.google.inject.Inject;
import com.hmdm.persistence.AbstractDAO;
import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import com.hmdm.plugins.devicetracker.model.DeviceLocationArchive;
import com.hmdm.security.SecurityContext;
import org.mybatis.guice.transactional.Transactional;
import java.util.List;
import java.util.Date;

/**
 * DAO для работы с местоположениями устройств
 */
public class DeviceLocationDAO extends AbstractDAO<DeviceLocation> {
    
    @Inject
    public DeviceLocationDAO() {
        super(DeviceLocation.class);
    }

    /**
     * Сохраняет текущее местоположение устройства
     */
    @Transactional
    public void saveLocation(DeviceLocation location) {
        SecurityContext.get().getCurrentUser().getCustomerId();
        getSecureSqlSession().insert("DeviceLocationMapper.insertLocation", location);
        getSecureSqlSession().insert("DeviceLocationMapper.insertHistory", location);
    }

    /**
     * Получает последнее местоположение устройства
     */
    public DeviceLocation getLastLocation(Long deviceId) {
        return getSecureSqlSession().selectOne("DeviceLocationMapper.getLastLocation", deviceId);
    }

    /**
     * Получает историю местоположений устройства за период
     */
    public List<DeviceLocation> getLocationHistory(Long deviceId, Date from, Date to) {
        return getSecureSqlSession().selectList("DeviceLocationMapper.getLocationHistory",
            new LocationHistoryRequest(deviceId, from, to));
    }

    /**
     * Очищает старую историю местоположений
     */
    @Transactional
    public void cleanupHistory(int retentionDays) {
        Date cutoffDate = new Date(System.currentTimeMillis() - retentionDays * 86400000L);
        getSecureSqlSession().delete("DeviceLocationMapper.cleanupHistory", cutoffDate);
    }

    private static class LocationHistoryRequest {
        private final Long deviceId;
        private final Date from;
        private final Date to;

        public LocationHistoryRequest(Long deviceId, Date from, Date to) {
            this.deviceId = deviceId;
            this.from = from;
            this.to = to;
        }

        public Long getDeviceId() { return deviceId; }
        public Date getFrom() { return from; }
        public Date getTo() { return to; }
    }

    /**
     * Сохраняет архивные данные о местоположениях
     */
    @Transactional
    public void saveArchive(DeviceLocationArchive archive) {
        getSecureSqlSession().insert("DeviceLocationMapper.insertArchive", archive);
    }

    /**
     * Получает архивы за указанный период
     */
    public List<DeviceLocationArchive> getArchives(Long deviceId, Date from, Date to) {
        return getSecureSqlSession().selectList("DeviceLocationMapper.getArchives",
            new LocationHistoryRequest(deviceId, from, to));
    }

    /**
     * Удаляет диапазон данных из истории
     */
    @Transactional
    public void deleteHistoryRange(Long deviceId, Date from, Date to) {
        getSecureSqlSession().delete("DeviceLocationMapper.deleteHistoryRange",
            new LocationHistoryRequest(deviceId, from, to));
    }

    /**
     * Получает список устройств, имеющих данные за указанный месяц
     */
    public List<Long> getDevicesWithHistory(Date monthDate) {
        return getSecureSqlSession().selectList("DeviceLocationMapper.getDevicesWithHistory", monthDate);
    }
}