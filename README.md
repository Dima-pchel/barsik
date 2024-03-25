# Barsik
discord funny bot

# Начальные приготовления

1. JDK 11
2. maven 3.6.2
3. mySQL версии 5.7

# Разворачивание бота

1. в MySQL создаем схему с названием barsik `CREATE SCHEMA 'test' DEFAULT CHARACTER SET utf8 ;`, в случае если пользователь схемы отличается от root/root, актуальные данные следует внести в application.properties в значения spring.datasource.username=root
   spring.datasource.password=root
2. Заполняем проперти в application.properties token значением актуального ключа бота. (В целях безопасности не стоит вносить этот ключ в комит)
3. Можно выполнять сборку и запуск приложения.

# Сборка из консоли

1. Выполняем в cmd: mvn clean install -U 
2. запускаем сервер: mvn spring-boot:run


# Памятка для развертывания на сервере

1.`fuser -k 8090/tcp` - для остановки приложения
2. Выполняем в cmd: mvn clean install -U
3. запускаем сервер: mvn spring-boot:run
4. `ctrl+z + bg` - выйти в фоновый режим 
5. disown -ah

