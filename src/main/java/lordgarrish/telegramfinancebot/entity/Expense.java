package lordgarrish.telegramfinancebot.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.Instant;
import java.util.Objects;

@Getter
@NoArgsConstructor
@Entity
@Table(name="expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expense_id")
    private Long id;
    @Column(name = "user_id")
    private long userId;
    @Column(name = "amount")
    private double amount;
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    @Column(name = "added_on", updatable = false)
    @org.hibernate.annotations.CreationTimestamp
    private Instant addedOn;

    public Expense(long userId, double amount, Category category) {
        this.userId = userId;
        this.amount = amount;
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Expense expense = (Expense) o;

        if (userId != expense.userId) return false;
        if (Double.compare(expense.amount, amount) != 0) return false;
        return Objects.equals(category, expense.category);
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (int) (userId ^ (userId >>> 32));
        temp = Double.doubleToLongBits(amount);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (category != null ? category.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "ID = " + id +
                ", User_ID = " + userId +
                ", Category = " + category.getTitle() +
                ", Amount = " + amount +
                ", Added_On = " + addedOn +
                '}';
    }
}
