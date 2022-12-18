package lordgarrish.telegramfinancebot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@NoArgsConstructor
@Getter
@Entity
public class ExpensesStatistics {

    @Id
    private String category;

    private double amount;
}
