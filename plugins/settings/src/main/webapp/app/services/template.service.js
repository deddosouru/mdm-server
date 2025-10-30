angular.module('plugin-settings')
    .factory('settingsTemplateService', ['$resource',
        function ($resource) {
            return $resource('rest/plugins/settings/templates/:id/:action', {
                id: '@id',
                action: '@action'
            }, {
                getAll: { method: 'GET', isArray: true },
                get: { method: 'GET' },
                save: { method: 'POST' },
                update: { method: 'PUT' },
                remove: { method: 'DELETE' },
                copy: { method: 'POST', params: { action: 'copy' } },
                rename: { method: 'PUT', params: { action: 'rename' } }
            });
        }
    ]);