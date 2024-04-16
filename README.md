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


# Деплой на сервере

Настроен автоматический деплой с помощью github Actions.

На сервере c помощью docker-compose развернуты отдельные контейнеры с базой данный и с ботом.

GitHub Runner взаимодествует только с контейнером бота.

Сборка контейнера бота:
Из корня проекта, где находится Dockerfile `docker build -t dimapchel/kets:1.0.0.3 .` 
>1.0.0.3 - версия контейнера, развернутого на сервере, на текущий момент не используется таг latest 

Push контейнера в DockerHub :
`docker push dimapchel/kets:1.0.0.3`


