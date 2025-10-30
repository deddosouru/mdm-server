package com.hmdm.plugins.settings.model;

public enum SettingType {
    // Настройки подключений
    WIFI("WiFi", "Настройки WiFi", true),
    BLUETOOTH("Bluetooth", "Настройки Bluetooth", true),
    MOBILE_DATA("MobileData", "Мобильные данные", true),
    HOTSPOT("Hotspot", "Точка доступа", true),
    
    // Системные настройки
    SCREEN_TIMEOUT("ScreenTimeout", "Время до блокировки экрана", true),
    SCREEN_BRIGHTNESS("ScreenBrightness", "Яркость экрана", true),
    VOLUME("Volume", "Громкость", true),
    DATE_TIME("DateTime", "Дата и время", true),
    TIMEZONE("Timezone", "Часовой пояс", true),
    LOCATION("Location", "Геолокация", true),
    
    // Безопасность
    PASSWORD_POLICY("PasswordPolicy", "Политика паролей", true),
    ENCRYPTION("Encryption", "Шифрование", true),
    SCREEN_LOCK("ScreenLock", "Блокировка экрана", true),
    USB_DEBUGGING("UsbDebugging", "Отладка по USB", true),
    UNKNOWN_SOURCES("UnknownSources", "Установка из неизвестных источников", true),
    
    // Управление приложениями
    APP_PERMISSIONS("AppPermissions", "Разрешения приложений", true),
    APP_RESTRICTIONS("AppRestrictions", "Ограничения приложений", true),
    DEFAULT_APPS("DefaultApps", "Приложения по умолчанию", true),
    
    // Ограничения
    CAMERA("Camera", "Камера", true),
    MICROPHONE("Microphone", "Микрофон", true),
    NFC("NFC", "NFC", true),
    AIRPLANE_MODE("AirplaneMode", "Режим полета", true),
    
    // Энергопотребление
    BATTERY_OPTIMIZATION("BatteryOptimization", "Оптимизация батареи", true),
    POWER_SAVING("PowerSaving", "Энергосбережение", true),
    
    // Сеть
    VPN("VPN", "VPN", true),
    PROXY("Proxy", "Прокси", true),
    DNS("DNS", "DNS", true),
    
    // Специальные возможности
    ACCESSIBILITY("Accessibility", "Специальные возможности", true),
    FONT_SIZE("FontSize", "Размер шрифта", true),
    DISPLAY_SIZE("DisplaySize", "Масштаб экрана", true),

    // Настройки ТСД
    SCANNER_ENABLED("ScannerEnabled", "Включение сканера", true),
    SCANNER_TRIGGER("ScannerTrigger", "Кнопка сканирования", true),
    SCANNER_SOUND("ScannerSound", "Звук сканирования", true),
    SCANNER_VIBRATION("ScannerVibration", "Вибрация при сканировании", true),
    SCANNER_ILLUMINATION("ScannerIllumination", "Подсветка сканера", true),
    SCANNER_AIM("ScannerAim", "Прицел сканера", true),
    SCANNER_DECODE_TIMEOUT("ScannerDecodeTimeout", "Таймаут декодирования", true),
    SCANNER_SYMBOLOGIES("ScannerSymbologies", "Поддерживаемые типы штрихкодов", true),
    SCANNER_OCR("ScannerOCR", "Распознавание текста", true),
    
    // Настройки кассового терминала
    FISCAL_PRINTER("FiscalPrinter", "Фискальный принтер", true),
    CASH_DRAWER("CashDrawer", "Денежный ящик", true),
    CUSTOMER_DISPLAY("CustomerDisplay", "Дисплей покупателя", true),
    CARD_READER("CardReader", "Считыватель карт", true),
    RECEIPT_PRINTER("ReceiptPrinter", "Принтер чеков", true),
    POS_KEYBOARD("POSKeyboard", "POS-клавиатура", true),
    SCALES("Scales", "Весы", true),
    
    // Специфические настройки периферии
    PRINTER_DPI("PrinterDPI", "Разрешение печати", true),
    PRINTER_DENSITY("PrinterDensity", "Плотность печати", true),
    PRINTER_SPEED("PrinterSpeed", "Скорость печати", true),
    DISPLAY_BRIGHTNESS("DisplayBrightness", "Яркость дисплея покупателя", true),
    DRAWER_OPEN_TIME("DrawerOpenTime", "Время открытия денежного ящика", true);

    private final String code;
    private final String description;
    private final boolean supported;

    SettingType(String code, String description, boolean supported) {
        this.code = code;
        this.description = description;
        this.supported = supported;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public boolean isSupported() {
        return supported;
    }
}