package lordgarrish.telegramfinancebot.service;

import lordgarrish.telegramfinancebot.entity.Category;
import lordgarrish.telegramfinancebot.entity.Expense;
import lordgarrish.telegramfinancebot.repository.ExpensesRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExpensesServiceTest {

    @Autowired
    private ExpensesService expensesService;

    @MockBean
    ExpensesRepository expensesRepo;

    @Test
    @DisplayName("Should save expense to database")
    void saveExpenseTest() {
        Expense expense = new Expense(42L, 1234, new Category(2L, "Products"));

        Mockito.doReturn(expense)
                .when(expensesRepo)
                .save(expense);

        Expense saved = expensesService.saveExpense(expense);
        assertEquals(expense, saved);

        Mockito.verify(expensesRepo, Mockito.times(1)).save(expense);
    }

    @Test
    @DisplayName("Should delete expense from database")
    void deleteExpenseTest() {
        Expense expense = new Expense(42L, 1234, new Category(2L, "Products"));

        Mockito.doReturn(Optional.of(expense))
                .when(expensesRepo)
                .findOneByUserId(expense.getUserId(), 123L);

        boolean isDeleted = expensesService.deleteExpense(expense.getUserId(), 123L);
        assertTrue(isDeleted);

        Mockito.verify(expensesRepo, Mockito.times(1))
                .deleteOneByUserId(expense.getUserId(), 123L);
    }

    @Test
    @DisplayName("Should return false when trying to delete non-existing expense from database")
    void deleteExpenseFailTest() {
        Expense expense = new Expense(42L, 1234, new Category(2L, "Products"));

        Mockito.doReturn(Optional.of(expense))
                .when(expensesRepo)
                .findOneByUserId(expense.getUserId(), 123L);

        boolean isDeleted = expensesService.deleteExpense(expense.getUserId(), 456L);
        assertFalse(isDeleted);

        Mockito.verify(expensesRepo, Mockito.times(0))
                .deleteOneByUserId(expense.getUserId(), 123L);
    }
}