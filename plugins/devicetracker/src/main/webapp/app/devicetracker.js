angular.module('plugin-devicetracker', ['ngResource', 'ui-notification'])
    .factory('deviceLocationService', ['$resource',
        function ($resource) {
            return $resource('rest/plugins/devicetracker/private/locations/:deviceId/:action', {
                deviceId: '@deviceId',
                action: '@action'
            }, {
                getCurrent: {method: 'GET', params: {action: 'current'}},
                getHistory: {method: 'GET', params: {action: 'history'}, isArray: true}
            });
        }
    ])
    .controller('DeviceLocationController', ['$scope', 'deviceLocationService', 'Notification',
        function ($scope, deviceLocationService, Notification) {
            var map = null;
            var markers = {};
            var pathLayer = null;

            $scope.initMap = function() {
                if (!map) {
                    map = L.map('devicemap').setView([0, 0], 2);
                    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
                        attribution: '© OpenStreetMap contributors'
                    }).addTo(map);
                }
            };

            $scope.showDeviceLocation = function(deviceId) {
                deviceLocationService.getCurrent({deviceId: deviceId}, function(response) {
                    if (response && response.lat && response.lon) {
                        $scope.showMarker(deviceId, response, true);
                    }
                });
            };

            $scope.showDeviceHistory = function(deviceId, from, to) {
                if (pathLayer) {
                    map.removeLayer(pathLayer);
                }

                deviceLocationService.getHistory({
                    deviceId: deviceId,
                    from: from.getTime(),
                    to: to.getTime()
                }, function(locations) {
                    if (locations && locations.length > 0) {
                        // Создаем линии маршрута
                        var points = locations.map(function(loc) {
                            return [loc.lat, loc.lon];
                        });

                        // Рисуем линии с градиентом цвета
                        pathLayer = L.polyline(points, {
                            color: '#2196F3',
                            weight: 3,
                            opacity: 0.8,
                            lineJoin: 'round'
                        }).addTo(map);

                        var stopZones = [];
                        var currentStopZone = null;
                        var stopStartIndex = null;
                        
                        // Добавляем маркеры и анализируем зоны стоянок
                        locations.forEach(function(loc, index) {
                            var isFirst = index === 0;
                            var isLast = index === locations.length - 1;
                            
                            // Обработка зон стоянки
                            if (loc.stoppedTime && loc.stoppedTime > 0) {
                                if (!currentStopZone) {
                                    currentStopZone = {
                                        center: [loc.lat, loc.lon],
                                        startTime: new Date(loc.timestamp),
                                        points: [loc]
                                    };
                                    stopStartIndex = index;
                                } else {
                                    currentStopZone.points.push(loc);
                                }
                            } else if (currentStopZone) {
                                // Завершаем текущую зону стоянки
                                finishStopZone(currentStopZone, locations[index - 1], stopStartIndex, index - 1);
                                stopZones.push(currentStopZone);
                                currentStopZone = null;
                            }
                            
                            // Особые маркеры для начала и конца маршрута
                            if (isFirst || isLast) {
                                var markerColor = isFirst ? 'green' : 'red';
                                var markerIcon = L.divIcon({
                                    className: 'route-marker ' + (isFirst ? 'start' : 'end'),
                                    html: '<div style="background-color: ' + markerColor + 
                                          '; width: 12px; height: 12px; border-radius: 50%; border: 2px solid white;"></div>',
                                    iconSize: [16, 16]
                                });
                                
                                L.marker([loc.lat, loc.lon], {icon: markerIcon})
                                    .bindPopup(getLocationPopup(loc, isFirst ? 'Start' : 'End'))
                                    .addTo(map);
                            }
                        });
                        
                        // Завершаем последнюю зону стоянки, если есть
                        if (currentStopZone) {
                            finishStopZone(currentStopZone, locations[locations.length - 1], 
                                         stopStartIndex, locations.length - 1);
                            stopZones.push(currentStopZone);
                        }
                        
                        // Отображаем зоны стоянки
                        stopZones.forEach(function(zone) {
                            // Создаем круг для зоны стоянки
                            var circle = L.circle(zone.center, {
                                color: '#FFA000',
                                fillColor: '#FFB74D',
                                fillOpacity: 0.3,
                                weight: 2,
                                radius: 100 // радиус в метрах
                            }).addTo(map);
                            
                            // Добавляем маркер с информацией о стоянке
                            var stopIcon = L.divIcon({
                                className: 'stop-marker',
                                html: '<div class="stop-point"></div>',
                                iconSize: [16, 16]
                            });
                            
                            L.marker(zone.center, {icon: stopIcon})
                                .bindPopup(getStopZonePopup(zone))
                                .addTo(map);
                        });

                        // Центрируем карту на маршруте
                        map.fitBounds(pathLayer.getBounds(), {padding: [50, 50]});
                    }
                });
            };

            $scope.showMarker = function(deviceId, location, center) {
                if (markers[deviceId]) {
                    map.removeLayer(markers[deviceId]);
                }

                var marker = L.marker([location.lat, location.lon])
                    .bindPopup(getLocationPopup(location))
                    .addTo(map);

                markers[deviceId] = marker;

                if (center) {
                    map.setView([location.lat, location.lon], 15);
                }
            };

            function getLocationPopup(location, label) {
                var content = '';
                if (label) {
                    content += '<strong>' + label + '</strong><br>';
                }
                content += 'Time: ' + new Date(location.timestamp).toLocaleString() + '<br>';
                
                // Показываем измеренную скорость, если есть
                if (location.speed) {
                    content += 'Measured Speed: ' + location.speed.toFixed(1) + ' km/h<br>';
                }
                
                // Показываем рассчитанную скорость
                if (location.calculatedSpeed) {
                    content += 'Average Speed: ' + location.calculatedSpeed.toFixed(1) + ' km/h<br>';
                }
                
                // Показываем время стоянки
                if (location.stoppedTime && location.stoppedTime > 0) {
                    var hours = Math.floor(location.stoppedTime / 3600);
                    var minutes = Math.floor((location.stoppedTime % 3600) / 60);
                    if (hours > 0) {
                        content += 'Stopped Time: ' + hours + 'h ' + minutes + 'm<br>';
                    } else {
                        content += 'Stopped Time: ' + minutes + 'm<br>';
                    }
                }
                
                // Показываем расстояние от предыдущей точки
                if (location.distance) {
                    content += 'Distance: ' + (location.distance > 1000 ? 
                        (location.distance / 1000).toFixed(1) + ' km' : 
                        Math.round(location.distance) + ' m') + '<br>';
                }
                
                if (location.accuracy) {
                    content += 'Accuracy: ' + location.accuracy + ' m<br>';
                }
                return content;
            }
            
            function finishStopZone(zone, lastPoint, startIndex, endIndex) {
                zone.endTime = new Date(lastPoint.timestamp);
                zone.duration = Math.floor((zone.endTime - zone.startTime) / 1000); // в секундах
                zone.startIndex = startIndex;
                zone.endIndex = endIndex;
                
                // Вычисляем центр зоны как среднее всех точек
                var totalLat = 0, totalLon = 0;
                zone.points.forEach(function(p) {
                    totalLat += p.lat;
                    totalLon += p.lon;
                });
                zone.center = [totalLat / zone.points.length, totalLon / zone.points.length];
            }
            
            function getStopZonePopup(zone) {
                var duration = zone.duration;
                var hours = Math.floor(duration / 3600);
                var minutes = Math.floor((duration % 3600) / 60);
                
                var content = '<div class="stop-zone-popup">';
                content += '<strong>Stop Zone</strong><br>';
                content += 'Start: ' + zone.startTime.toLocaleString() + '<br>';
                content += 'End: ' + zone.endTime.toLocaleString() + '<br>';
                content += 'Duration: ';
                
                if (hours > 0) {
                    content += hours + 'h ' + minutes + 'm';
                } else {
                    content += minutes + 'm';
                }
                
                if (zone.points && zone.points.length > 0) {
                    var firstPoint = zone.points[0];
                    if (firstPoint.provider) {
                        content += '<br>Source: ' + firstPoint.provider;
                    }
                    if (firstPoint.accuracy) {
                        content += '<br>Accuracy: ±' + Math.round(firstPoint.accuracy) + 'm';
                    }
                }
                
                content += '</div>';
                return content;
            }
        }
    ]);