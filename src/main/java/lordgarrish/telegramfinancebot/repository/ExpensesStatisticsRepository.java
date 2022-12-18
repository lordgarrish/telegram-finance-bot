package lordgarrish.telegramfinancebot.repository;

import lordgarrish.telegramfinancebot.entity.ExpensesStatistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.*;

public interface ExpensesStatisticsRepository extends JpaRepository<ExpensesStatistics, String> {

    @Query(value = "SELECT SUM(amount) AS amount, title AS category FROM expenses JOIN categories USING(category_id)" +
            " WHERE user_id = :chat_id AND DATE(added_on) = CURRENT_DATE GROUP BY category", nativeQuery = true)
    List<ExpensesStatistics> expensesStatisticsForToday(@Param("chat_id") Long chatId);

    @Query(value = "SELECT SUM(amount) AS amount, title AS category FROM expenses JOIN categories USING(category_id)" +
            " WHERE user_id = :chat_id AND EXTRACT(MONTH FROM added_on) = EXTRACT(MONTH FROM CURRENT_DATE) GROUP BY category",
            nativeQuery = true)
    List<ExpensesStatistics> expensesStatisticsForMonth(@Param("chat_id") Long chatId);

    @Query(value = "SELECT SUM(amount) FROM expenses WHERE user_id = :chat_id AND DATE(added_on) = CURRENT_DATE",
            nativeQuery = true)
    Optional<Double> totalAmountForToday(@Param("chat_id") Long chatId);

    @Query(value = "SELECT SUM(amount) FROM expenses WHERE user_id = :chat_id AND EXTRACT(MONTH FROM added_on) " +
            "= EXTRACT(MONTH FROM CURRENT_DATE)", nativeQuery = true)
    Optional<Double> totalAmountForMonth(@Param("chat_id") Long chatId);
}
