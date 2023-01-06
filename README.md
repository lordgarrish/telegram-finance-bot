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
`bot.name` - your bot name<br>
`bot.key` - bot token<br>
`spring.datasource.url` - database url<br>
`spring.datasource.username` - your database user<br>
`spring.datasource.password` - your database password<br><br>
SQL code for creating database tables provided in `db-script.sql`
in project root folder.
### Docker
For launching this bot inside Docker container cd to project root folder and enter in
your terminal:
```
docker compose up
```
To enter pqsl (PostgreSQL shell):
```
docker exec -it pgdb psql -h localhost -p 5432 -d <your db> -U <your user>
```