package lordgarrish.telegramfinancebot.repository;

import lordgarrish.telegramfinancebot.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}
