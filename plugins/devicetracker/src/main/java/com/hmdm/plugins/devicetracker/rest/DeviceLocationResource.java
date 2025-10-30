package com.hmdm.plugins.devicetracker.rest;

import com.google.inject.Inject;
import com.hmdm.persistence.DeviceDAO;
import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import com.hmdm.plugins.devicetracker.model.LocationDetails;
import com.hmdm.plugins.devicetracker.persistence.DeviceLocationDAO;
import com.hmdm.plugins.devicetracker.service.DeviceLocationArchiveService;
import com.hmdm.plugins.devicetracker.util.LocationAnalyzer;
import com.hmdm.rest.json.Response;
import com.hmdm.security.SecurityContext;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Api(tags = {"Device Location Plugin"})
@Path("/plugins/devicetracker/private")
@Produces(MediaType.APPLICATION_JSON)
public class DeviceLocationResource {
    
    private static final Logger log = LoggerFactory.getLogger(DeviceLocationResource.class);
    
    private final DeviceLocationDAO locationDAO;
    private final DeviceLocationArchiveService archiveService;
    
    @Inject
    public DeviceLocationResource(DeviceLocationDAO locationDAO,
                                DeviceLocationArchiveService archiveService) {
        this.locationDAO = locationDAO;
        this.archiveService = archiveService;
    }
    
    @GET
    @Path("/locations/{deviceId}/current")
    @ApiOperation("Получить текущее местоположение устройства")
    public Response getCurrentLocation(@PathParam("deviceId") Long deviceId) {
        try {
            DeviceLocation location = locationDAO.getLastLocation(deviceId);
            return Response.OK(location);
        } catch (Exception e) {
            log.error("Failed to get current location for device " + deviceId, e);
            return Response.INTERNAL_ERROR();
        }
    }
    
    @GET
    @Path("/locations/{deviceId}/history")
    @ApiOperation("Получить историю местоположений устройства")
    public Response getLocationHistory(@PathParam("deviceId") Long deviceId,
                                     @QueryParam("from") Long fromTime,
                                     @QueryParam("to") Long toTime) {
        try {
            Date from = new Date(fromTime);
            Date to = new Date(toTime);
            
            // Получаем данные из текущей истории
            List<DeviceLocation> currentHistory = locationDAO.getLocationHistory(deviceId, from, to);
            
            // Получаем данные из архива
            List<DeviceLocation> archivedHistory = archiveService.getArchivedLocations(deviceId, from, to);
            
            // Объединяем списки
            currentHistory.addAll(archivedHistory);
            
            // Сортируем по времени и анализируем
            currentHistory.sort((a, b) -> a.getTimestamp().compareTo(b.getTimestamp()));
            List<LocationDetails> analyzedLocations = LocationAnalyzer.analyzeLocations(currentHistory);
            
            return Response.OK(analyzedLocations);
        } catch (Exception e) {
            log.error("Failed to get location history for device " + deviceId, e);
            return Response.INTERNAL_ERROR();
        }
    }
}