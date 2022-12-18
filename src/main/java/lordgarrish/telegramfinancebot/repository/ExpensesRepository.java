package lordgarrish.telegramfinancebot.repository;

import lordgarrish.telegramfinancebot.entity.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface ExpensesRepository extends JpaRepository<Expense, Long> {

    @Query(value = "SELECT * FROM expenses WHERE user_id = :chat_id ORDER BY added_on DESC LIMIT 5",
            nativeQuery = true)
    List<Expense> findLastFive(@Param("chat_id") Long chatId);

    @Query(value = "SELECT e FROM Expense e WHERE e.userId = :chat_id AND e.id = :id")
    Optional<Expense> findOneByUserId(@Param("chat_id") Long chatId, @Param("id") Long expenseID);

    @Modifying
    @Query(value = "DELETE FROM Expense e WHERE e.userId = :chat_id AND e.id = :id")
    void deleteOneByUserId(@Param("chat_id") Long chatId, @Param("id") Long expenseID);
}
