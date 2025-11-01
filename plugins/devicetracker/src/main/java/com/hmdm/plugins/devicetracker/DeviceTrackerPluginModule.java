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

package com.hmdm.plugins.devicetracker;

import com.google.inject.AbstractModule;
import com.hmdm.plugins.devicetracker.persistence.DeviceLocationDAO;
import com.hmdm.plugins.devicetracker.persistence.mapper.DeviceLocationMapper;
import com.hmdm.plugins.devicetracker.rest.DeviceLocationResource;
import com.hmdm.plugins.devicetracker.service.DeviceLocationService;

/**
 * <p>A Guice module for Device Tracker plugin.</p>
 *
 * @author isv
 */
public class DeviceTrackerPluginModule extends AbstractModule {

    /**
     * <p>Constructs new <code>DeviceTrackerPluginModule</code> instance. This implementation does nothing.</p>
     */
    public DeviceTrackerPluginModule() {
    }

    /**
     * <p>Configures the bindings for plugin components.</p>
     */
    @Override
    protected void configure() {
        bind(DeviceLocationMapper.class);
        bind(DeviceLocationDAO.class);
        bind(DeviceLocationService.class);
        bind(DeviceLocationResource.class);
    }
}