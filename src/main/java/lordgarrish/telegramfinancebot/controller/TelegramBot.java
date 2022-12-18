package lordgarrish.telegramfinancebot.controller;

import lombok.extern.slf4j.Slf4j;
import lordgarrish.telegramfinancebot.config.BotConfig;
import lordgarrish.telegramfinancebot.entity.*;
import lordgarrish.telegramfinancebot.service.*;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.MessageEntity;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.text.DecimalFormat;
import java.util.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    final BotConfig config;
    final ExpensesService expensesService;
    final CategoriesService categoriesService;
    final UserService userService;
    final ExpensesStatisticsService expensesStatisticsService;

    public TelegramBot(BotConfig config, ExpensesService expensesService, CategoriesService categoriesService,
                       UserService userService, ExpensesStatisticsService expensesStatisticsService) {
        this.config = config;
        this.expensesService = expensesService;
        this.categoriesService = categoriesService;
        this.userService = userService;
        this.expensesStatisticsService = expensesStatisticsService;
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasCallbackQuery()) {
            handleCallback(update.getCallbackQuery());
        } else if(update.hasMessage()) {
            if(update.getMessage().hasText() && update.getMessage().hasEntities()) {
                handleBotCommand(update.getMessage());
            } else if(update.getMessage().hasText()) {
                handleMessage(update.getMessage());
            }
        }
    }

    private void handleMessage(Message message) {
        long chatId = message.getChatId();
        Optional<Double> amount = parseUserInput(message);
        if (amount.isPresent()) {
            Category category = expensesService.getCurrentExpenseCategory(chatId);
            Expense expense = new Expense(chatId, amount.get(), category);
            expensesService.saveExpense(expense);
            double todayAmount = expensesStatisticsService.getSumOfExpensesForToday(chatId).orElse(0.0);
            String reply = "Added expense: amount - " + getAmountFormat(expense.getAmount()) + " rub, category - " +
                    expense.getCategory().getTitle() + "\n\n" + "Total expenses for today: " +
                    getAmountFormat(todayAmount) + " rub";
            sendReplyMessage(chatId, reply);
        } else {
            sendReplyMessage(chatId, "Please, enter the sum in correct numeric format, i.e. 1488.69");
        }
    }

    private void handleCallback(CallbackQuery callbackQuery) {
        Message message = callbackQuery.getMessage();
        Long categoryId = Long.parseLong(callbackQuery.getData());
        Category category = categoriesService.findCategoryById(categoryId);
        expensesService.saveCurrentExpenseCategory(message.getChatId(), category);
        log.info("User " + message.getChatId() + " changed the expense category to: " + category);
        List<List<InlineKeyboardButton>> buttons = getKeyboard(message);
        try {
            execute(EditMessageReplyMarkup.builder()
                    .chatId(message.getChatId())
                    .messageId(message.getMessageId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error occurred in 'handleCallback' method:\n" + e.getMessage());
        }
    }

    private void handleBotCommand(Message message) {
        Optional<MessageEntity> commandEntity =
                message.getEntities().stream().filter(e -> e.getType().equals("bot_command")).findFirst();
        if(commandEntity.isPresent()) {
            String command = message.getText().substring(commandEntity.get().getOffset(), commandEntity.get().getLength());
            switch (command) {
                case "/start":
                    log.info("User " + message.getChatId() + " entered '/start' command");
                    handleStartCommand(message);
                    break;
                case "/category":
                    log.info("User " + message.getChatId() + " entered '/category' command");
                    handleCategoriesCommand(message);
                    break;
                case "/today":
                    log.info("User " + message.getChatId() + " entered '/today' command");
                    handleTodayExpensesCommand(message);
                    break;
                case "/month":
                    log.info("User " + message.getChatId() + " entered '/month' command");
                    handleMonthlyExpensesCommand(message);
                    break;
                case "/latest":
                    log.info("User " + message.getChatId() + " entered '/latest' command");
                    handleLatestExpensesCommand(message);
                    break;
                default:
                    if(command.startsWith("/del")) {
                        log.info("User " + message.getChatId() + " entered " + command + " command");
                        handleDeleteCommand(command.substring(4), message);
                    } else {
                        log.warn("User " + message.getChatId() + " entered unknown command: " + command);
                        sendReplyMessage(message.getChatId(), "Incorrect command");
                    }
            }
        }
    }

    private void handleStartCommand(Message message) {
        long chatId = message.getChatId();
        String firstName = message.getChat().getFirstName();
        String lastName = message.getChat().getLastName();
        String username = message.getChat().getUserName();
        User user = new User(chatId, firstName, lastName, username);
        if(userService.saveUser(user)) {
            log.info(user + " launches the bot for the first time!");
        }
        String answer = firstName + ", welcome!\n\n" +
                "This is a personal finance management bot. \n\n" +
                "/category - Choose category\n" +
                "/today - Expenses statistics for today\n" +
                "/month - Expenses statistics for the last month\n" +
                "/latest - Recently added expenses";
        sendReplyMessage(chatId, answer);
    }

    private void handleCategoriesCommand(Message message) {
        List<List<InlineKeyboardButton>> buttons = getKeyboard(message);
        try {
            execute(SendMessage.builder()
                    .text("Please choose expenses category:")
                    .chatId(message.getChatId())
                    .replyMarkup(InlineKeyboardMarkup.builder().keyboard(buttons).build())
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error occurred in 'handleCategoriesCommand' method:\n" + e.getMessage());
        }
    }

    private void handleTodayExpensesCommand(Message message) {
        double sum = expensesStatisticsService.getSumOfExpensesForToday(message.getChatId()).orElse(0.0);
        var statistics = expensesStatisticsService.getExpensesStatisticsForToday(message.getChatId());
        String textToSend = prepareStatistics("today", sum, statistics);
        sendReplyMessage(message.getChatId(), textToSend);
    }

    private void handleMonthlyExpensesCommand(Message message) {
        double sum = expensesStatisticsService.getSumOfExpensesForMonth(message.getChatId()).orElse(0.0);
        var statistics = expensesStatisticsService.getExpensesStatisticsForMonth(message.getChatId());
        String textToSend = prepareStatistics("month", sum, statistics);
        sendReplyMessage(message.getChatId(), textToSend);
    }

    private void handleLatestExpensesCommand(Message message) {
        List<Expense> expenses = expensesService.getLatestExpenses(message.getChatId());
        StringBuilder reply = new StringBuilder("Latest expenses:\n\n");
        if(expenses.isEmpty()) {
            reply.append("No recent expenses were found.");
        } else {
            for (Expense exp : expenses) {
                var str = exp.getCategory().getTitle() + " - " + getAmountFormat(exp.getAmount()) + " rub. Press /del" +
                        exp.getId() + " to delete entry." + "\n\n";
                reply.append(str);
            }
        }
        sendReplyMessage(message.getChatId(), reply.toString());
    }

    private void handleDeleteCommand(String strId, Message message) {
        long id;
        try {
            id = Long.parseLong(strId);
        } catch (NumberFormatException e) {
            sendReplyMessage(message.getChatId(), "Incorrect command");
            return;
        }
        if(expensesService.deleteExpense(message.getChatId(), id)) {
            sendReplyMessage(message.getChatId(), "Expense has been removed");
        } else {
            sendReplyMessage(message.getChatId(), "Expense not found");
        }
    }

    private String prepareStatistics(String period, double sum, List<ExpensesStatistics> statistics) {
        String header = "Total expenses for " + period + ": " + getAmountFormat(sum) + " rub\n\n" +
                "Expenses by categories:\n\n";
        if(sum == 0) {
            return header + "No expenses for this period";
        }
        StringBuilder reply = new StringBuilder(header);
        for(var stat : statistics) {
            String line = stat.getCategory() + ": total amount - " + getAmountFormat(stat.getAmount()) +
                    " rub\n\n";
            reply.append(line);
        }
        return reply.toString();
    }

    private void sendReplyMessage(long chatId, String textToSend) {
        try {
            execute(SendMessage.builder()
                    .chatId(chatId)
                    .text(textToSend)
                    .build());
        } catch (TelegramApiException e) {
            log.error("Error occurred in 'sendReplyMessage' method: " + e.getMessage());
        }
    }

    private List<List<InlineKeyboardButton>> getKeyboard(Message message) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        for(var category : categoriesService.getCategories()) {
            buttons.add(Arrays.asList(InlineKeyboardButton.builder()
                    .text(getCategoryButton(message.getChatId(), category))
                    .callbackData(category.getId().toString())
                    .build()));
        }
        return buttons;
    }

    private String getCategoryButton(long chatId, Category current) {
        Category saved = expensesService.getCurrentExpenseCategory(chatId);
        return current.equals(saved) ? current.getTitle() + " ✅" : current.getTitle();
    }

    private String getAmountFormat(double amount) {
        DecimalFormat decFormat = new DecimalFormat();
        decFormat.setMaximumFractionDigits(2);
        return decFormat.format(amount);
    }

    private Optional<Double> parseUserInput(Message message) {
        String messageText = message.getText();
        try {
            var amount = Optional.of(Double.parseDouble(messageText));
            log.info("User " + message.getChatId() + " entered amount:  " + amount.get());
            return amount;
        } catch (NumberFormatException e) {
            log.info("User " + message.getChatId() + " wrote message: " + messageText);
            return Optional.empty();
        }
    }
}
