package lordgarrish.telegramfinancebot.service;

import lordgarrish.telegramfinancebot.entity.ExpensesStatistics;
import lordgarrish.telegramfinancebot.repository.ExpensesStatisticsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class ExpensesStatisticsService {

    @Autowired
    ExpensesStatisticsRepository expensesStatisticsRepository;

    public List<ExpensesStatistics> getExpensesStatisticsForToday(Long chatId) {
        return expensesStatisticsRepository.expensesStatisticsForToday(chatId);
    }

    public List<ExpensesStatistics> getExpensesStatisticsForMonth(Long chatId) {
        return expensesStatisticsRepository.expensesStatisticsForMonth(chatId);
    }

    public Optional<Double> getSumOfExpensesForToday(Long chatId) {
        return expensesStatisticsRepository.totalAmountForToday(chatId);
    }

    public Optional<Double> getSumOfExpensesForMonth(Long chatId) {
        return expensesStatisticsRepository.totalAmountForMonth(chatId);
    }
}
