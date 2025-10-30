package com.hmdm.plugins.devicetracker.service;

import com.google.inject.Inject;
import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import com.hmdm.plugins.devicetracker.model.DeviceLocationArchive;
import com.hmdm.plugins.devicetracker.persistence.DeviceLocationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class DeviceLocationArchiveService {
    private static final Logger log = LoggerFactory.getLogger(DeviceLocationArchiveService.class);
    
    private final DeviceLocationDAO locationDAO;
    
    @Inject
    public DeviceLocationArchiveService(DeviceLocationDAO locationDAO) {
        this.locationDAO = locationDAO;
    }
    
    /**
     * Архивирует данные за указанный месяц
     */
    public void archiveMonth(Long deviceId, int year, int month) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.set(year, month - 1, 1, 0, 0, 0);
            Date startDate = cal.getTime();
            
            cal.add(Calendar.MONTH, 1);
            Date endDate = cal.getTime();
            
            // Получаем все точки за месяц
            List<DeviceLocation> locations = locationDAO.getLocationHistory(deviceId, startDate, endDate);
            if (locations.isEmpty()) {
                return;
            }
            
            // Сериализуем и сжимаем данные
            byte[] compressedData = compressLocations(locations);
            
            // Создаем архивную запись
            DeviceLocationArchive archive = new DeviceLocationArchive();
            archive.setDeviceId(deviceId);
            archive.setYear(year);
            archive.setMonth(month);
            archive.setData(compressedData);
            archive.setCompressed(true);
            archive.setPointsCount(locations.size());
            archive.setCreatedAt(new Date());
            
            // Сохраняем архив
            locationDAO.saveArchive(archive);
            
            // Удаляем заархивированные данные из основной таблицы
            locationDAO.deleteHistoryRange(deviceId, startDate, endDate);
            
        } catch (Exception e) {
            log.error("Failed to archive location data for device " + deviceId, e);
            throw new RuntimeException("Archive creation failed", e);
        }
    }
    
    /**
     * Получает данные из архива за указанный период
     */
    public List<DeviceLocation> getArchivedLocations(Long deviceId, Date from, Date to) {
        List<DeviceLocation> result = new ArrayList<>();
        
        try {
            // Получаем все архивные записи за период
            List<DeviceLocationArchive> archives = locationDAO.getArchives(deviceId, from, to);
            
            // Распаковываем данные из каждого архива
            for (DeviceLocationArchive archive : archives) {
                List<DeviceLocation> locations = decompressLocations(archive.getData());
                // Фильтруем точки по датам
                for (DeviceLocation loc : locations) {
                    if (loc.getTimestamp().after(from) && loc.getTimestamp().before(to)) {
                        result.add(loc);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to retrieve archived locations for device " + deviceId, e);
            throw new RuntimeException("Archive retrieval failed", e);
        }
        
        return result;
    }
    
    /**
     * Сжимает список местоположений
     */
    private byte[] compressLocations(List<DeviceLocation> locations) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (GZIPOutputStream gzos = new GZIPOutputStream(baos);
             ObjectOutputStream oos = new ObjectOutputStream(gzos)) {
            oos.writeObject(locations);
        }
        return baos.toByteArray();
    }
    
    /**
     * Распаковывает список местоположений
     */
    @SuppressWarnings("unchecked")
    private List<DeviceLocation> decompressLocations(byte[] data) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
             GZIPInputStream gzis = new GZIPInputStream(bais);
             ObjectInputStream ois = new ObjectInputStream(gzis)) {
            return (List<DeviceLocation>) ois.readObject();
        }
    }
}