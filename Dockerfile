FROM openjdk:18.0.2.1

EXPOSE 8080

RUN mkdir ./tg_moneybot

COPY ./target/telegram-finance-bot-0.0.1-SNAPSHOT.jar ./tg_moneybot

ENTRYPOINT ["java", "-jar", "./tg_moneybot/telegram-finance-bot-0.0.1-SNAPSHOT.jar"]