# telegram-finance-bot
Telegram bot for personal finance management. Made with Java 18, Spring Boot 2,
Spring Data, PostgreSQL and [Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots).<br><br>
After starting the bot, type `/category` in the bot menu and then enter correct amount.<br>
Other bot commands:<br>
`/today` - Expenses statistics for today<br>
`/month` - Expenses statistics for the last month<br>
`/latest` - Recently added expense<br>
### Usage
Create <code>application.properties</code> file in <code>src/main/resources</code>
folder to set up bot credentials and database settings.<br><br>
`bot.name` - bot name<br>
`bot.key` - bot token<br>
`spring.datasource.url` - database url<br>
`spring.datasource.username` - database user<br>
`spring.datasource.password` - database password<br><br>
SQL code for creating database tables provided in `db-script.sql`
in project root folder.<br><br>
Build project using Maven:
```
mvn clean package
```
Launch bot on your local machine:
```
java -jar target/telegram-finance-bot-0.0.1-SNAPSHOT.jar
```
### Docker
For launching this bot inside Docker container go to project root folder and enter in
your terminal:
```
docker compose up
```
This command will also create database called 'tg_finance_bot' for you. To 
set a different name for your DB, go to `docker-compose.yml` and change `POSTGRES_DB`
property.

To enter pqsl (PostgreSQL shell):
```
docker exec -it pgdb psql -h localhost -p 5432 -d <YOUR_DB> -U <YOUR_USER>
```