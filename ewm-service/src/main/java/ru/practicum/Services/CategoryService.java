package ru.practicum.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.CategoryDto;
import ru.practicum.storage.CategoryRepository;

import static ru.practicum.mapper.CategoryMapper.categoryToDto;
import static ru.practicum.mapper.CategoryMapper.dtoToCategory;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryDto postCategory(CategoryDto categoryDto) {
        return categoryToDto(categoryRepository.save(dtoToCategory(categoryDto)));

    }

}
