package com.hmdm.plugins.devicetracker.util;

import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import com.hmdm.plugins.devicetracker.model.LocationDetails;

import java.util.ArrayList;
import java.util.List;

public class LocationAnalyzer {
    private static final double EARTH_RADIUS = 6371000; // Радиус Земли в метрах
    private static final double STOP_SPEED_THRESHOLD = 1.0; // Порог скорости для определения стоянки (км/ч)
    private static final double STOP_DISTANCE_THRESHOLD = 100; // Порог расстояния для определения стоянки (метры)
    private static final double MIN_MOVEMENT_DISTANCE = 100; // Минимальное расстояние для записи движения (метры)
    
    /**
     * Анализирует точки маршрута и добавляет дополнительную информацию
     */
    public static List<LocationDetails> analyzeLocations(List<DeviceLocation> locations) {
        List<LocationDetails> result = new ArrayList<>();
        if (locations == null || locations.isEmpty()) {
            return result;
        }
        
        LocationDetails prevPoint = null;
        LocationDetails currPoint = null;
        
        for (DeviceLocation loc : locations) {
            currPoint = new LocationDetails(loc);
            
            if (prevPoint != null) {
                // Рассчитываем расстояние между точками
                double distance = calculateDistance(
                    prevPoint.getLat(), prevPoint.getLon(),
                    currPoint.getLat(), currPoint.getLon()
                );
                currPoint.setDistance(distance);
                
                // Рассчитываем время между точками в часах
                double timeDiff = (currPoint.getTimestamp().getTime() - 
                                 prevPoint.getTimestamp().getTime()) / 3600000.0;
                
                boolean isSignificantMovement = distance >= MIN_MOVEMENT_DISTANCE;
                
                // Рассчитываем скорость только для значимых перемещений
                if (timeDiff > 0) {
                    if (isSignificantMovement) {
                        double speed = (distance / 1000.0) / timeDiff;
                        currPoint.setCalculatedSpeed(speed);
                    } else {
                        // Для незначительных перемещений считаем скорость нулевой
                        currPoint.setCalculatedSpeed(0.0);
                    }
                }
                
                // Определяем время стоянки
                if (!isSignificantMovement || isStopPoint(currPoint, prevPoint)) {
                    long stoppedSeconds = (currPoint.getTimestamp().getTime() - 
                                         prevPoint.getTimestamp().getTime()) / 1000;
                    currPoint.setStoppedTime(
                        prevPoint.getStoppedTime() != null ? 
                        prevPoint.getStoppedTime() + stoppedSeconds : 
                        stoppedSeconds
                    );
                    
                    // Для точек стоянки используем координаты предыдущей точки
                    // чтобы избежать "дрожания" маркера на карте
                    if (!isSignificantMovement) {
                        currPoint.setLat(prevPoint.getLat());
                        currPoint.setLon(prevPoint.getLon());
                    }
                }
            } else {
                currPoint.setStoppedTime(0L);
                currPoint.setDistance(0.0);
                currPoint.setCalculatedSpeed(0.0);
            }
            
            result.add(currPoint);
            prevPoint = currPoint;
        }
        
        return result;
    }
    
    /**
     * Определяет, является ли точка частью стоянки
     */
    private static boolean isStopPoint(LocationDetails curr, LocationDetails prev) {
        // Проверяем скорость и расстояние
        return (curr.getDistance() != null && curr.getDistance() < STOP_DISTANCE_THRESHOLD) ||
               (curr.getSpeed() != null && curr.getSpeed() < STOP_SPEED_THRESHOLD) ||
               (curr.getCalculatedSpeed() != null && curr.getCalculatedSpeed() < STOP_SPEED_THRESHOLD);
    }
    
    /**
     * Рассчитывает расстояние между двумя точками по формуле гаверсинусов
     */
    private static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                  Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                  Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }
}