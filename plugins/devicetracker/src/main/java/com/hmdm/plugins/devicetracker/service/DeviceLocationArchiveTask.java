package com.hmdm.plugins.devicetracker.service;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.hmdm.plugins.devicetracker.persistence.DeviceLocationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Singleton
public class DeviceLocationArchiveTask implements ServletContextListener {
    private static final Logger log = LoggerFactory.getLogger(DeviceLocationArchiveTask.class);
    
    private final DeviceLocationArchiveService archiveService;
    private final DeviceLocationDAO locationDAO;
    private ScheduledExecutorService executor;
    
    @Inject
    public DeviceLocationArchiveTask(DeviceLocationArchiveService archiveService, 
                                   DeviceLocationDAO locationDAO) {
        this.archiveService = archiveService;
        this.locationDAO = locationDAO;
    }
    
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        executor = Executors.newSingleThreadScheduledExecutor();
        // Запускаем задачу в 2 часа ночи каждый день
        executor.scheduleAtFixedRate(this::archiveOldData, 
            getInitialDelay(), 24 * 60 * 60, TimeUnit.SECONDS);
    }
    
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        if (executor != null) {
            executor.shutdown();
        }
    }
    
    private void archiveOldData() {
        try {
            Calendar cal = Calendar.getInstance();
            // Архивируем данные за предыдущий месяц
            cal.add(Calendar.MONTH, -1);
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH) + 1;
            
            // Получаем список всех устройств с данными за этот месяц
            List<Long> deviceIds = locationDAO.getDevicesWithHistory(cal.getTime());
            
            for (Long deviceId : deviceIds) {
                try {
                    archiveService.archiveMonth(deviceId, year, month);
                } catch (Exception e) {
                    log.error("Failed to archive data for device " + deviceId, e);
                }
            }
        } catch (Exception e) {
            log.error("Failed to run archive task", e);
        }
    }
    
    private long getInitialDelay() {
        Calendar now = Calendar.getInstance();
        Calendar nextRun = Calendar.getInstance();
        
        nextRun.set(Calendar.HOUR_OF_DAY, 2);
        nextRun.set(Calendar.MINUTE, 0);
        nextRun.set(Calendar.SECOND, 0);
        
        if (now.after(nextRun)) {
            nextRun.add(Calendar.DAY_OF_MONTH, 1);
        }
        
        return (nextRun.getTimeInMillis() - now.getTimeInMillis()) / 1000;
    }
}