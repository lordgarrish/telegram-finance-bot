Telegram bot for personal finance management. Made with Java 18, Spring Boot 2,
Spring Data, PostgreSQL and [Telegram Bot Java Library](https://github.com/rubenlagus/TelegramBots).<br><br>
Create <code>application.properties</code> file in <code>src/main/resources</code>
folder to set up bot credentials and database settings.<br><br>
<code>bot.name</code> - your bot name<br>
<code>bot.key</code> - bot token<br><br>
SQL code for creating database tables provided in <code>db-script.sql</code>
in root folder.
### Usage
Type <code>/category</code> in the bot menu and then enter correct amount.<br>
Other bot commands:<br>
<code>/today</code> - Expenses statistics for today<br>
<code>/month</code> - Expenses statistics for the last month<br>
<code>/latest</code> - Recently added expense<br>