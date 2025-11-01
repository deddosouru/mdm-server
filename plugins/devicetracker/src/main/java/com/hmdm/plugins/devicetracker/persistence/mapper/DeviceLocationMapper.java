/*
 *
 * Headwind MDM: Open Source Android MDM Software
 * https://h-mdm.com
 *
 * Copyright (C) 2019 Headwind Solutions LLC (http://h-sms.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hmdm.plugins.devicetracker.persistence.mapper;

import com.hmdm.plugins.devicetracker.model.DeviceLocation;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>An ORM mapper for {@link DeviceLocation} domain objects.</p>
 *
 * @author isv
 */
public interface DeviceLocationMapper {

    /**
     * <p>Inserts or updates the current location of the device.</p>
     *
     * @param location a location to be saved.
     */
    void insertLocation(DeviceLocation location);

    /**
     * <p>Inserts the history record for the device location.</p>
     *
     * @param location a location to be saved to history.
     */
    void insertHistory(DeviceLocation location);

    /**
     * <p>Gets the last recorded location for the specified device.</p>
     *
     * @param deviceId an ID of the device.
     * @return last recorded location for the specified device or <code>null</code> if no location was recorded yet.
     */
    DeviceLocation getLastLocation(@Param("deviceId") Long deviceId);

    /**
     * <p>Gets the location history for the specified device.</p>
     *
     * @param deviceId an ID of the device.
     * @param from     a beginning of the period to get the history for.
     * @param to       an ending of the period to get the history for.
     * @return a list of location history records for the specified device.
     */
    List<DeviceLocation> getLocationHistory(@Param("deviceId") Long deviceId,
                                            @Param("from") long from,
                                            @Param("to") long to);

    /**
     * <p>Deletes the old history records which are older than specified date.</p>
     *
     * @param cutoffDate a date to delete the records older than.
     * @return a number of deleted records.
     */
    int cleanupHistory(@Param("cutoffDate") Date cutoffDate);
}