angular.module('plugin-settings')
    .controller('TemplateEditorController', ['$scope', '$state', '$stateParams', 'settingsTemplateService', 'Notification',
        function ($scope, $state, $stateParams, settingsTemplateService, Notification) {
            $scope.template = null;
            $scope.settingTypes = [];
            $scope.editMode = false;
            $scope.groups = [];
            $scope.configurations = [];
            
            // Группировка настроек по категориям
            $scope.categories = {
                NETWORK: { name: 'Сеть', settings: [] },
                SYSTEM: { name: 'Система', settings: [] },
                SECURITY: { name: 'Безопасность', settings: [] },
                SCANNER: { name: 'Сканер', settings: [] },
                POS: { name: 'Кассовое оборудование', settings: [] },
                PERIPHERALS: { name: 'Периферия', settings: [] }
            };

            // Загрузка типов настроек и их распределение по категориям
            function loadSettingTypes() {
                settingsTemplateService.getSettingTypes().$promise.then(function(types) {
                    $scope.settingTypes = types;
                    
                    types.forEach(function(type) {
                        if (type.code.startsWith('SCANNER')) {
                            $scope.categories.SCANNER.settings.push(type);
                        } else if (type.code.startsWith('WIFI') || type.code.startsWith('BLUETOOTH') || 
                                 type.code.startsWith('MOBILE') || type.code.startsWith('VPN')) {
                            $scope.categories.NETWORK.settings.push(type);
                        } else if (type.code.includes('PRINTER') || type.code.includes('DRAWER') || 
                                 type.code.includes('DISPLAY') || type.code.includes('POS')) {
                            $scope.categories.POS.settings.push(type);
                        } else if (type.code.includes('PASSWORD') || type.code.includes('SECURITY') || 
                                 type.code.includes('ENCRYPTION')) {
                            $scope.categories.SECURITY.settings.push(type);
                        } else if (type.code.startsWith('PRINTER') || type.code.startsWith('DISPLAY') || 
                                 type.code.startsWith('DRAWER')) {
                            $scope.categories.PERIPHERALS.settings.push(type);
                        } else {
                            $scope.categories.SYSTEM.settings.push(type);
                        }
                    });
                });
            }

            // Загрузка шаблона
            function loadTemplate() {
                if ($stateParams.id) {
                    settingsTemplateService.get({ id: $stateParams.id }).$promise.then(function(template) {
                        $scope.template = template;
                        initializeSettings();
                    }).catch(function() {
                        Notification.error('Ошибка загрузки шаблона');
                    });
                } else {
                    $scope.template = {
                        name: 'Новый шаблон',
                        description: '',
                        settings: [],
                        active: true,
                        applyMode: 'OVERRIDE'
                    };
                    initializeSettings();
                }
            }

            // Инициализация настроек шаблона
            function initializeSettings() {
                if (!$scope.template.settings) {
                    $scope.template.settings = [];
                }
                
                // Создаем map существующих настроек для быстрого доступа
                var settingsMap = {};
                $scope.template.settings.forEach(function(s) {
                    settingsMap[s.type] = s;
                });

                // Добавляем отсутствующие настройки со значением по умолчанию
                $scope.settingTypes.forEach(function(type) {
                    if (!settingsMap[type.code]) {
                        $scope.template.settings.push({
                            type: type.code,
                            key: type.code.toLowerCase(),
                            value: '',
                            description: '',
                            mandatory: false,
                            validationRule: 'NONE'
                        });
                    }
                });
            }

            // Сохранение шаблона
            $scope.saveTemplate = function() {
                // Удаляем пустые настройки
                $scope.template.settings = $scope.template.settings.filter(function(s) {
                    return s.value !== '';
                });

                var promise = $scope.template.id ?
                    settingsTemplateService.update({ id: $scope.template.id }, $scope.template).$promise :
                    settingsTemplateService.save($scope.template).$promise;

                promise.then(function(saved) {
                    Notification.success('Шаблон сохранен');
                    if (!$scope.template.id) {
                        $state.go('templates.edit', { id: saved.id });
                    }
                }).catch(function() {
                    Notification.error('Ошибка сохранения шаблона');
                });
            };

            // Получение значения настройки
            $scope.getSettingValue = function(settingType) {
                var setting = $scope.template.settings.find(function(s) {
                    return s.type === settingType.code;
                });
                return setting ? setting.value : '';
            };

            // Установка значения настройки
            $scope.setSettingValue = function(settingType, value) {
                var setting = $scope.template.settings.find(function(s) {
                    return s.type === settingType.code;
                });
                if (setting) {
                    setting.value = value;
                }
            };

            // Проверка обязательности настройки
            $scope.isSettingMandatory = function(settingType) {
                var setting = $scope.template.settings.find(function(s) {
                    return s.type === settingType.code;
                });
                return setting ? setting.mandatory : false;
            };

            // Загрузка данных при инициализации
            loadSettingTypes();
            loadTemplate();
        }
    ]);