package lordgarrish.telegramfinancebot.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long id;
    @Column(name = "title")
    private String title;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;

        return Objects.equals(title, category.title);
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0;
    }
}
