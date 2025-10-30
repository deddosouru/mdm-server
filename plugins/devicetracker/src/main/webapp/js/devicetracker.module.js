angular.module('plugin-devicetracker', ['ngResource', 'ui-notification'])
.config(['$stateProvider',
    function ($stateProvider) {
        $stateProvider.state('plugin-devicetracker', {
            url: '/plugin-devicetracker',
            templateUrl: 'app/components/plugins/devicetracker/views/devicetracker.html',
            controller: 'DeviceTrackerController'
        });
    }
])
.controller('DeviceTrackerController', ['$scope', '$http', 'Notification',
    function ($scope, $http, Notification) {
        $scope.devices = [];
        $scope.selectedDevice = null;
        $scope.map = null;
        $scope.markers = {};
        $scope.historyPath = null;
        $scope.dateFrom = new Date();
        $scope.dateTo = new Date();
        
        // Инициализация карты OpenStreetMap
        function initMap() {
            $scope.map = L.map('map').setView([55.7558, 37.6173], 10);
            L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                attribution: '© OpenStreetMap contributors'
            }).addTo($scope.map);
        }
        
        // Загрузка списка устройств
        function loadDevices() {
            $http.get('rest/private/devices/search')
                .then(function(response) {
                    $scope.devices = response.data.data;
                })
                .catch(function(error) {
                    Notification.error('Failed to load devices');
                });
        }
        
        // Обновление местоположения устройства
        function updateDeviceLocation(deviceId) {
            $http.get('rest/plugins/devicetracker/location/' + deviceId)
                .then(function(response) {
                    var location = response.data.data;
                    if (location) {
                        if ($scope.markers[deviceId]) {
                            $scope.markers[deviceId].setLatLng([location.latitude, location.longitude]);
                        } else {
                            var marker = L.marker([location.latitude, location.longitude])
                                .bindPopup(getDeviceName(deviceId))
                                .addTo($scope.map);
                            $scope.markers[deviceId] = marker;
                        }
                        $scope.map.setView([location.latitude, location.longitude]);
                    }
                })
                .catch(function(error) {
                    Notification.error('Failed to update device location');
                });
        }
        
        // Загрузка истории местоположений
        function loadHistory() {
            if (!$scope.selectedDevice) return;
            
            var from = $scope.dateFrom.getTime();
            var to = $scope.dateTo.getTime();
            
            $http.get('rest/plugins/devicetracker/history/' + $scope.selectedDevice.id 
                     + '?from=' + from + '&to=' + to)
                .then(function(response) {
                    var history = response.data.data;
                    if (history && history.length > 0) {
                        // Очищаем предыдущий путь
                        if ($scope.historyPath) {
                            $scope.map.removeLayer($scope.historyPath);
                        }
                        
                        // Создаем новый путь
                        var points = history.map(function(loc) {
                            return [loc.latitude, loc.longitude];
                        });
                        
                        $scope.historyPath = L.polyline(points, {
                            color: 'red',
                            weight: 3,
                            opacity: 0.5
                        }).addTo($scope.map);
                        
                        // Подстраиваем масштаб карты под маршрут
                        $scope.map.fitBounds($scope.historyPath.getBounds());
                    }
                })
                .catch(function(error) {
                    Notification.error('Failed to load location history');
                });
        }
        
        function getDeviceName(deviceId) {
            var device = $scope.devices.find(function(d) { return d.id === deviceId; });
            return device ? device.name || device.number : 'Unknown device';
        }
        
        // Инициализация
        initMap();
        loadDevices();
        
        // Обновляем местоположения каждые 30 секунд
        setInterval(function() {
            $scope.devices.forEach(function(device) {
                updateDeviceLocation(device.id);
            });
        }, 30000);
        
        // Публичные методы
        $scope.selectDevice = function(device) {
            $scope.selectedDevice = device;
            updateDeviceLocation(device.id);
        };
        
        $scope.showHistory = function() {
            loadHistory();
        };
    }
]);