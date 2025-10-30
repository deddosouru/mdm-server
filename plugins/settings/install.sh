#!/bin/bash

# Установка плагина настроек устройств
echo "Installing Device Settings Plugin..."

# Проверяем наличие необходимых переменных окружения
if [ -z "$HMDM_HOME" ]; then
    echo "Error: HMDM_HOME environment variable is not set"
    exit 1
fi

if [ -z "$TOMCAT_HOME" ]; then
    echo "Error: TOMCAT_HOME environment variable is not set"
    exit 1
fi

# Создаем временную директорию
TMP_DIR=$(mktemp -d)
cd $TMP_DIR

# Компилируем плагин
echo "Building plugin..."
cd $HMDM_HOME/plugins/settings
mvn clean install

if [ $? -ne 0 ]; then
    echo "Error: Failed to build plugin"
    exit 1
fi

# Копируем файлы плагина
echo "Copying plugin files..."
cp target/settings-1.0.0.jar $TOMCAT_HOME/webapps/ROOT/WEB-INF/lib/

# Применяем миграции базы данных
echo "Applying database migrations..."
PGPASSWORD=$DB_PASSWORD psql -h $DB_HOST -U $DB_USER -d $DB_NAME -f src/main/resources/liquibase/settings.changelog.xml

if [ $? -ne 0 ]; then
    echo "Error: Failed to apply database migrations"
    exit 1
fi

# Очищаем временную директорию
rm -rf $TMP_DIR

echo "Device Settings Plugin installation completed successfully"
exit 0