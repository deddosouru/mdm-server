package com.hmdm.plugins.devicetracker.rest;

import com.hmdm.persistence.UnsecureDAO;
import com.hmdm.persistence.domain.Device;
import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import com.hmdm.plugins.devicetracker.persistence.DeviceLocationDAO;
import com.hmdm.plugins.devicetracker.service.DeviceTrackerService;
import com.hmdm.rest.json.Response;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;
import java.util.List;

@Api(tags = {"Device Tracker Plugin"})
@Path("/plugins/devicetracker")
public class DeviceTrackerResource {

    private static final Logger log = LoggerFactory.getLogger(DeviceTrackerResource.class);

    private final DeviceLocationDAO deviceLocationDAO;
    private final DeviceTrackerService deviceTrackerService;
    private final UnsecureDAO unsecureDAO;

    @Inject
    public DeviceTrackerResource(DeviceLocationDAO deviceLocationDAO,
                                DeviceTrackerService deviceTrackerService,
                                UnsecureDAO unsecureDAO) {
        this.deviceLocationDAO = deviceLocationDAO;
        this.deviceTrackerService = deviceTrackerService;
        this.unsecureDAO = unsecureDAO;
    }

    @PUT
    @Path("/location/{deviceNumber}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Обновляет местоположение устройства")
    public Response updateLocation(@PathParam("deviceNumber") String deviceNumber,
                                 DeviceLocation location) {
        try {
            Device device = this.unsecureDAO.getDeviceByNumber(deviceNumber);
            if (device == null) {
                return Response.ERROR("error.device.not.found");
            }

            location.setDeviceId(device.getId());
            location.setTimestamp(new Date());
            
            this.deviceTrackerService.updateLocation(location);
            
            return Response.OK();
        } catch (Exception e) {
            log.error("Failed to update device location", e);
            return Response.ERROR("error.internal");
        }
    }

    @GET
    @Path("/location/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Получает последнее местоположение устройства")
    public Response getLastLocation(@PathParam("deviceId") Long deviceId) {
        try {
            DeviceLocation location = this.deviceLocationDAO.getLastLocation(deviceId);
            return Response.OK(location);
        } catch (Exception e) {
            log.error("Failed to get device location", e);
            return Response.ERROR("error.internal");
        }
    }

    @GET
    @Path("/history/{deviceId}")
    @Produces(MediaType.APPLICATION_JSON)
    @ApiOperation("Получает историю местоположений устройства")
    public Response getLocationHistory(@PathParam("deviceId") Long deviceId,
                                     @QueryParam("from") Long fromTime,
                                     @QueryParam("to") Long toTime) {
        try {
            Date from = fromTime != null ? new Date(fromTime) : new Date(0);
            Date to = toTime != null ? new Date(toTime) : new Date();
            
            List<DeviceLocation> history = this.deviceLocationDAO.getLocationHistory(deviceId, from, to);
            return Response.OK(history);
        } catch (Exception e) {
            log.error("Failed to get device location history", e);
            return Response.ERROR("error.internal");
        }
    }
}