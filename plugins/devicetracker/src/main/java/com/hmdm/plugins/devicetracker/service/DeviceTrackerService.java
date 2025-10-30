package com.hmdm.plugins.devicetracker.service;

import com.google.inject.Inject;
import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import com.hmdm.plugins.devicetracker.model.DeviceTrackerSettings;
import com.hmdm.plugins.devicetracker.persistence.DeviceLocationDAO;
import com.hmdm.plugins.devicetracker.persistence.DeviceTrackerSettingsDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DeviceTrackerService {
    private static final Logger log = LoggerFactory.getLogger(DeviceTrackerService.class);
    
    private final DeviceLocationDAO locationDAO;
    private final DeviceTrackerSettingsDAO settingsDAO;
    private final ScheduledExecutorService scheduler;
    
    @Inject
    public DeviceTrackerService(DeviceLocationDAO locationDAO,
                               DeviceTrackerSettingsDAO settingsDAO) {
        this.locationDAO = locationDAO;
        this.settingsDAO = settingsDAO;
        this.scheduler = Executors.newScheduledThreadPool(1);
        
        // Запускаем периодическую очистку старых данных
        this.scheduler.scheduleAtFixedRate(this::cleanupHistory, 1, 24, TimeUnit.HOURS);
    }
    
    public void updateLocation(DeviceLocation location) {
        DeviceTrackerSettings settings = settingsDAO.getSettings(location.getDeviceId());
        
        // Проверяем точность
        if (location.getAccuracy() != null && 
            location.getAccuracy() > settings.getMinAccuracy()) {
            log.debug("Location accuracy {} is worse than required {}", 
                     location.getAccuracy(), settings.getMinAccuracy());
            return;
        }
        
        // Проверяем минимальное расстояние от предыдущей точки
        DeviceLocation lastLocation = locationDAO.getLastLocation(location.getDeviceId());
        if (lastLocation != null && settings.getMinDistance() > 0) {
            double distance = calculateDistance(
                lastLocation.getLatitude(), lastLocation.getLongitude(),
                location.getLatitude(), location.getLongitude()
            );
            
            if (distance < settings.getMinDistance()) {
                log.debug("Distance {} is less than minimum required {}", 
                         distance, settings.getMinDistance());
                return;
            }
        }
        
        // Сохраняем местоположение
        locationDAO.saveLocation(location);
    }
    
    private void cleanupHistory() {
        try {
            // Получаем настройки хранения для каждого клиента
            settingsDAO.getAllSettings().forEach(settings -> {
                try {
                    locationDAO.cleanupHistory(settings.getRetentionDays());
                } catch (Exception e) {
                    log.error("Failed to cleanup history for customer {}", 
                             settings.getCustomerId(), e);
                }
            });
        } catch (Exception e) {
            log.error("Failed to cleanup location history", e);
        }
    }
    
    /**
     * Вычисляет расстояние между двумя точками по формуле гаверсинусов
     */
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371000; // Радиус Земли в метрах
        
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
}