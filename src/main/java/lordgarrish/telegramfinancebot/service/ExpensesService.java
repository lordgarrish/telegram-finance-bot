package lordgarrish.telegramfinancebot.service;

import lombok.extern.slf4j.Slf4j;
import lordgarrish.telegramfinancebot.entity.Category;
import lordgarrish.telegramfinancebot.entity.Expense;
import lordgarrish.telegramfinancebot.repository.ExpensesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@Transactional
public class ExpensesService {

    @Autowired
    ExpensesRepository expensesRepository;
    private final Map<Long, Category> currentExpenseCategory = new HashMap<>();

    public Category getCurrentExpenseCategory(long userId) {
        return currentExpenseCategory.getOrDefault(userId, new Category(1L, "Other"));
    }

    public void saveCurrentExpenseCategory(long userId, Category category) {
        currentExpenseCategory.put(userId, category);
    }

    public Expense saveExpense(Expense expense) {
        Expense exp = expensesRepository.save(expense);
        log.info("Saved expense: " + expense);
        return exp;
    }

    public List<Expense> getLatestExpenses(long userId) {
        return expensesRepository.findLastFive(userId);
    }

    public boolean deleteExpense(long userId, long expenseId) {
        if(expensesRepository.findOneByUserId(userId, expenseId).isPresent()) {
            expensesRepository.deleteOneByUserId(userId, expenseId);
            log.info("Deleted expense with ID " + expenseId + " for user " + userId);
            return true;
        }
        log.warn("Expense with ID " + expenseId + " not found in database");
        return false;
    }
}
