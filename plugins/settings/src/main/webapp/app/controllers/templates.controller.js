angular.module('plugin-settings')
    .controller('TemplatesController', ['$scope', 'settingsTemplateService', 'Notification',
        function ($scope, settingsTemplateService, Notification) {
            $scope.templates = [];
            $scope.selectedTemplate = null;

            // Загрузка всех шаблонов
            $scope.loadTemplates = function() {
                settingsTemplateService.getAll().$promise.then(function(templates) {
                    $scope.templates = templates;
                }).catch(function(error) {
                    Notification.error('Ошибка загрузки шаблонов');
                });
            };

            // Создание нового шаблона
            $scope.createTemplate = function() {
                var template = new settingsTemplateService({
                    name: 'Новый шаблон',
                    active: true,
                    settings: [],
                    applyMode: 'OVERRIDE'
                });

                template.$save().then(function(saved) {
                    $scope.templates.push(saved);
                    Notification.success('Шаблон создан');
                }).catch(function(error) {
                    Notification.error('Ошибка создания шаблона');
                });
            };

            // Удаление шаблона
            $scope.deleteTemplate = function(template) {
                if (confirm('Вы уверены, что хотите удалить шаблон "' + template.name + '"?')) {
                    settingsTemplateService.remove({ id: template.id }).$promise.then(function() {
                        var idx = $scope.templates.indexOf(template);
                        if (idx !== -1) {
                            $scope.templates.splice(idx, 1);
                        }
                        if ($scope.selectedTemplate === template) {
                            $scope.selectedTemplate = null;
                        }
                        Notification.success('Шаблон удален');
                    }).catch(function(error) {
                        Notification.error('Ошибка удаления шаблона');
                    });
                }
            };

            // Копирование шаблона
            $scope.copyTemplate = function(template) {
                settingsTemplateService.copy({ id: template.id }).$promise.then(function(copy) {
                    $scope.templates.push(copy);
                    Notification.success('Шаблон скопирован');
                }).catch(function(error) {
                    Notification.error('Ошибка копирования шаблона');
                });
            };

            // Переименование шаблона
            $scope.renameTemplate = function(template) {
                var newName = prompt('Введите новое имя шаблона:', template.name);
                if (newName && newName !== template.name) {
                    settingsTemplateService.rename({ id: template.id, name: newName }).$promise
                        .then(function(updated) {
                            template.name = updated.name;
                            Notification.success('Шаблон переименован');
                        }).catch(function(error) {
                            Notification.error('Ошибка переименования шаблона');
                        });
                }
            };

            // Загрузка шаблонов при инициализации
            $scope.loadTemplates();
        }
    ]);