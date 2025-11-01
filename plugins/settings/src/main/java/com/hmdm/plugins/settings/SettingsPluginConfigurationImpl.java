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

import com.google.inject.Module;
import com.hmdm.plugin.PluginConfiguration;
import com.hmdm.plugin.PluginTaskModule;

import javax.servlet.ServletContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>A plugin configuration implementation for Settings plugin.</p>
 *
 * @author isv
 */
public class SettingsPluginConfigurationImpl implements PluginConfiguration {

    /**
     * <p>Constructs new <code>SettingsPluginConfigurationImpl</code> instance. This implementation does nothing.</p>
     */
    public SettingsPluginConfigurationImpl() {
    }

    /**
     * <p>Gets the identifier of the plugin. This identifier is used to distinguish the plugins in the system.</p>
     *
     * @return a plugin identifier.
     */
    @Override
    public String getPluginId() {
        return "settings";
    }

    /**
     * <p>Gets the list of Guice modules to be used for configuring the bindings for plugin components.</p>
     *
     * @param context a context of plugin initialization.
     * @return a list of Guice modules to be used for plugin initialization.
     */
    @Override
    public List<Module> getPluginModules(ServletContext context) {
        List<Module> modules = new ArrayList<>();
        modules.add(new SettingsPluginModule());
        return modules;
    }

    /**
     * <p>Gets the list of task modules to be used for initializing the background tasks for plugin.</p>
     *
     * @param context a context of plugin initialization.
     * @return a list of task modules to be used for plugin initialization.
     */
    @Override
    public Optional<List<Class<? extends PluginTaskModule>>> getTaskModules(ServletContext context) {
        return Optional.empty();
    }
}