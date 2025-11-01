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

package com.hmdm.plugins.settings;

import com.google.inject.AbstractModule;
import com.hmdm.plugins.settings.persistence.SettingsDAO;
import com.hmdm.plugins.settings.persistence.TemplateDAO;
import com.hmdm.plugins.settings.persistence.mapper.SettingsMapper;
import com.hmdm.plugins.settings.rest.SettingsResource;
import com.hmdm.plugins.settings.service.SettingsService;

/**
 * <p>A Guice module for Settings plugin.</p>
 *
 * @author isv
 */
public class SettingsPluginModule extends AbstractModule {

    /**
     * <p>Constructs new <code>SettingsPluginModule</code> instance. This implementation does nothing.</p>
     */
    public SettingsPluginModule() {
    }

    /**
     * <p>Configures the bindings for plugin components.</p>
     */
    @Override
    protected void configure() {
        bind(SettingsMapper.class);
        bind(SettingsDAO.class);
        bind(TemplateDAO.class);
        bind(SettingsService.class);
        bind(SettingsResource.class);
    }
}