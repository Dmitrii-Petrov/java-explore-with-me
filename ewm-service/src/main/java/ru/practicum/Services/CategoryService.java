package ru.practicum.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.CategoryDto;
import ru.practicum.exceptions.BadEntityException;
import ru.practicum.exceptions.FailNameException;
import ru.practicum.exceptions.NotFoundEntityException;
import ru.practicum.model.Category;
import ru.practicum.storage.CategoryRepository;
import ru.practicum.storage.EventRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.mapper.CategoryMapper.categoryToDto;
import static ru.practicum.mapper.CategoryMapper.dtoToCategory;

@Service
@RequiredArgsConstructor
@Transactional
public class CategoryService {

    private final CategoryRepository categoryRepository;

    private final EventRepository eventRepository;

    public CategoryDto postCategory(CategoryDto categoryDto) {
        if (categoryRepository.existsByName(categoryDto.getName())) {
            throw new FailNameException("there is no such category");
        }
        Category category = dtoToCategory(categoryDto);
        return categoryToDto(categoryRepository.save(category));

    }

    public void deleteCategory(Long id) {
        if (eventRepository.existsAllByCategoryId(id)) {
            throw new BadEntityException("there are events with this category");
        }
        categoryRepository.deleteById(id);
    }

    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {

        Category category = categoryRepository.findById(catId).get();
        Category category1 = categoryRepository.findByName(categoryDto.getName());


        if ((category1 != null) && (!(catId).equals(category1.getId()))) {
            throw new FailNameException("there is such category already");
        }

        if (categoryDto.getName() != null) {
            category.setName(categoryDto.getName());
        }
        return categoryToDto(categoryRepository.save(category));
    }


    public List<CategoryDto> getCategories(Integer from,
                                           Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        List<CategoryDto> result = new ArrayList<>();
        Page<Category> categoryPage = categoryRepository.findAll(page);
        categoryPage.getContent().forEach(category -> {
            result.add(categoryToDto(category));
        });
        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();
    }


    public CategoryDto getCategory(Long catId) {
        if (!categoryRepository.existsById(catId)) {
            throw new NotFoundEntityException("Category with id=" + catId + "was not found");
        } else return categoryToDto(categoryRepository.findById(catId).get());

    }
}
