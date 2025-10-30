# Device Settings Plugin

## Описание
Плагин для управления настройками устройств в системе Headwind MDM.

## Возможности
- Управление системными настройками устройств
- Настройка ТСД и кассового оборудования
- Создание и применение шаблонов настроек
- Групповое применение настроек

## Требования
- Headwind MDM Server версии 0.1.0 или выше
- PostgreSQL 9.6 или выше
- Java 8 или выше
- Maven 3.6 или выше

## Установка

### Автоматическая установка
1. Установите переменные окружения:
   ```bash
   export HMDM_HOME=/path/to/hmdm
   export TOMCAT_HOME=/path/to/tomcat
   export DB_HOST=localhost
   export DB_NAME=hmdm
   export DB_USER=hmdm
   export DB_PASSWORD=your_password
   ```

2. Запустите скрипт установки:
   ```bash
   ./install.sh
   ```

### Ручная установка
1. Скомпилируйте плагин:
   ```bash
   cd plugins/settings
   mvn clean install
   ```

2. Скопируйте JAR файл:
   ```bash
   cp target/settings-1.0.0.jar $TOMCAT_HOME/webapps/ROOT/WEB-INF/lib/
   ```

3. Примените миграции базы данных:
   ```bash
   psql -U hmdm -d hmdm -f src/main/resources/liquibase/settings.changelog.xml
   ```

## Конфигурация
1. После установки войдите в панель администратора Headwind MDM
2. Перейдите в раздел "Плагины"
3. Найдите "Device Settings Plugin" и активируйте его
4. Настройте необходимые параметры в интерфейсе плагина

## Использование
1. Создание шаблонов настроек:
   - Перейдите в раздел "Шаблоны настроек"
   - Нажмите "Создать шаблон"
   - Заполните необходимые параметры
   - Сохраните шаблон

2. Применение настроек:
   - Выберите целевые устройства/группы/конфигурации
   - Выберите шаблон настроек
   - Укажите режим применения
   - Примените настройки

## Поддерживаемые настройки
- Системные настройки (экран, звук, время)
- Сетевые настройки (WiFi, Bluetooth, мобильные данные)
- Настройки безопасности
- Настройки сканера ТСД
- Настройки кассового оборудования
- Настройки периферийных устройств

## Устранение неполадок
1. Проверьте логи Tomcat:
   ```bash
   tail -f $TOMCAT_HOME/logs/catalina.out
   ```

2. Проверьте статус плагина в интерфейсе администратора

3. Убедитесь, что все необходимые права доступа настроены правильно

## Удаление
1. Удалите JAR файл:
   ```bash
   rm $TOMCAT_HOME/webapps/ROOT/WEB-INF/lib/settings-1.0.0.jar
   ```

2. Удалите таблицы плагина из базы данных:
   ```sql
   DROP TABLE plugin_settings_templates CASCADE;
   DROP TABLE plugin_settings_device_configs CASCADE;
   DROP TABLE plugin_settings_template_devices CASCADE;
   DROP TABLE plugin_settings_template_groups CASCADE;
   DROP TABLE plugin_settings_template_configurations CASCADE;
   ```

## Поддержка
При возникновении проблем:
1. Проверьте документацию
2. Создайте issue в репозитории проекта
3. Обратитесь в техническую поддержку