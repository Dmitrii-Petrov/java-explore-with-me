package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.CategoryDto;
import ru.practicum.model.Category;

@Component
@AllArgsConstructor
public class CategoryMapper {

    public static CategoryDto categoryToDto (Category category){
        return new CategoryDto(category.getId(), category.getName());
    }


    public static Category dtoToCategory (CategoryDto categoryDto){
        Category category = new Category();
        category.setName(category.getName());
        return category;
    }

}
