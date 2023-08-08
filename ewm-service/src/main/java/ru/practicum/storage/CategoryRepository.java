package ru.practicum.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
