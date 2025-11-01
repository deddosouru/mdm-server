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

package com.hmdm.plugins.settings.persistence.mapper;

import com.hmdm.plugins.settings.model.DeviceConfig;
import com.hmdm.plugins.settings.model.ConfigTemplate;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>An ORM mapper for settings plugin domain objects.</p>
 *
 * @author isv
 */
public interface SettingsMapper {

    /**
     * <p>Gets the configurations for the specified device.</p>
     *
     * @param deviceId   an ID of the device.
     * @param customerId an ID of the customer account.
     * @return a list of configurations for the specified device.
     */
    List<DeviceConfig> getDeviceConfigs(@Param("deviceId") Long deviceId, @Param("customerId") int customerId);

    /**
     * <p>Gets the pending configurations for the specified device.</p>
     *
     * @param deviceId   an ID of the device.
     * @param customerId an ID of the customer account.
     * @return a list of pending configurations for the specified device.
     */
    List<DeviceConfig> getPendingConfigs(@Param("deviceId") Long deviceId, @Param("customerId") int customerId);

    /**
     * <p>Inserts a new configuration.</p>
     *
     * @param config a configuration to be inserted.
     */
    void insertConfig(DeviceConfig config);

    /**
     * <p>Updates the configuration.</p>
     *
     * @param config a configuration to be updated.
     */
    void updateConfig(DeviceConfig config);

    /**
     * <p>Updates the status of the configuration.</p>
     *
     * @param config a configuration to be updated.
     */
    void updateConfigStatus(DeviceConfig config);

    /**
     * <p>Gets the templates for the specified customer.</p>
     *
     * @param customerId an ID of the customer account.
     * @return a list of templates for the specified customer.
     */
    List<ConfigTemplate> getTemplates(@Param("customerId") int customerId);

    /**
     * <p>Inserts a new template.</p>
     *
     * @param template a template to be inserted.
     */
    void insertTemplate(ConfigTemplate template);

    /**
     * <p>Updates the template.</p>
     *
     * @param template a template to be updated.
     */
    void updateTemplate(ConfigTemplate template);

    /**
     * <p>Deletes the template.</p>
     *
     * @param id         an ID of the template to be deleted.
     * @param customerId an ID of the customer account.
     */
    void deleteTemplate(@Param("id") Long id, @Param("customerId") int customerId);
}