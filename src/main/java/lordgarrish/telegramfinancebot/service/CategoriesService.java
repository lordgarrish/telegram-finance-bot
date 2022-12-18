package lordgarrish.telegramfinancebot.service;

import lordgarrish.telegramfinancebot.entity.Category;
import lordgarrish.telegramfinancebot.repository.CategoriesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoriesService {

    @Autowired
    CategoriesRepository categoriesRepository;

    public List<Category> getCategories() {
        return categoriesRepository.findAll();
    }

    public Category findCategoryById(Long categoryId) {
        return categoriesRepository.findById(categoryId).orElse(new Category(1L, "Other"));
    }
}
