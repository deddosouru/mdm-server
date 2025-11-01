# Инструкция по установке Headwind MDM на Ubuntu

## Системные требования

- Ubuntu Server 20.04 LTS / 22.04 LTS / 24.04 LTS (рекомендуется 22.04 LTS)
- Минимум 2 ГБ RAM
- Минимум 20 ГБ свободного места на диске
- Процессор с поддержкой 64-bit архитектуры
- Подключение к интернету
- Публичный IP адрес или домен

## Предварительная подготовка

1. Обновите систему:
```bash
sudo apt update
sudo apt upgrade -y
```

2. Установите необходимые пакеты:
```bash
sudo apt install -y git maven postgresql openjdk-11-jdk
```

3. Убедитесь, что порты 8080 и 8443 не заняты другими сервисами:
```bash
sudo netstat -tulpn | grep -E '8080|8443'
```

## Установка и настройка базы данных

1. Создайте пользователя и базу данных PostgreSQL:
```bash
sudo -u postgres psql
```

2. В консоли PostgreSQL выполните:
```sql
CREATE USER hmdm WITH PASSWORD 'your_password';
CREATE DATABASE hmdm WITH OWNER=hmdm;
\q
```

## Скачивание и сборка исходного кода

1. Клонируйте репозиторий:
```bash
git clone https://github.com/h-mdm/hmdm-server
cd hmdm-server
```

2. Соберите проект:
```bash
mvn install
```

## Установка и настройка

1. Запустите скрипт установки:
```bash
sudo ./hmdm_install.sh
```

2. В процессе установки укажите:
- Язык установки (en/ru)
- Параметры подключения к PostgreSQL (хост, порт, имя базы, пользователь, пароль)
- Директорию для хранения файлов (рекомендуется оставить значение по умолчанию)
- Домен или IP-адрес сервера
- Настройки SMTP (опционально, для восстановления пароля)

## Настройка SSL (рекомендуется)

При установке выберите опцию настройки HTTPS через Let's Encrypt. Скрипт автоматически:
- Установит certbot
- Получит SSL сертификат
- Настроит Tomcat для использования SSL
- Добавит перенаправление с 80 на 8080 порт и с 443 на 8443 порт

## Проверка установки

1. Откройте в браузере:
- `http://your-domain:8080` (если SSL не настроен)
- `https://your-domain:8443` (если SSL настроен)

2. Войдите с учетными данными по умолчанию:
- Логин: `admin`
- Пароль: `admin`

## Безопасность

После успешной установки:

1. Смените пароль администратора
2. Настройте файрвол:
```bash
sudo ufw enable
sudo ufw allow ssh
sudo ufw allow 80/tcp
sudo ufw allow 443/tcp
sudo ufw allow 8080/tcp
sudo ufw allow 8443/tcp
```

## Обновление Java

Рекомендуется обновить настройки Java для улучшения производительности:

1. Отредактируйте файл:
```bash
sudo nano /etc/java-11-openjdk/security/java.security
```

2. Замените строку:
```
securerandom.source=file:/dev/random
```
на:
```
securerandom.source=file:/dev/urandom
```

## Устранение проблем

### Если Tomcat не запускается:
1. Проверьте логи:
```bash
sudo tail -f /var/log/tomcat*/catalina.out
```

2. Проверьте права доступа:
```bash
sudo chown -R tomcat: /var/lib/tomcat*
```

### Если не работает SSL:
1. Проверьте статус certbot:
```bash
sudo certbot certificates
```

2. Проверьте конфигурацию Tomcat:
```bash
sudo nano /var/lib/tomcat*/conf/server.xml
```

## Полезные команды

- Перезапуск Tomcat:
```bash
sudo systemctl restart tomcat*
```

- Проверка статуса Tomcat:
```bash
sudo systemctl status tomcat*
```

- Просмотр логов:
```bash
sudo tail -f /var/lib/tomcat*/logs/catalina.out
```

## Автоматическое обновление сертификатов

Скрипт установки автоматически добавляет задачу в crontab для обновления SSL сертификатов. 
Проверить наличие задачи можно командой:
```bash
crontab -l | grep letsencrypt-ssl
```

## Дополнительные рекомендации

1. Настройте регулярное резервное копирование:
- База данных:
```bash
pg_dump -U hmdm hmdm > backup_$(date +%Y%m%d).sql
```
- Файлы конфигурации и загруженные файлы:
```bash
tar -czf mdm_files_$(date +%Y%m%d).tar.gz /opt/hmdm
```

2. Настройте мониторинг:
- Использования диска
- Нагрузки на CPU
- Использования памяти
- Доступности сервиса

3. Настройте ротацию логов:
```bash
sudo nano /etc/logrotate.d/tomcat
```

## Дополнительная информация

- Документация: https://h-mdm.com/ru/documentation/
- Исходный код: https://github.com/h-mdm/hmdm-server
- Сообщество: https://t.me/h_mdm