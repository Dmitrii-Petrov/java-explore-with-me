package ru.practicum.Services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.DTO.UserDto;
import ru.practicum.model.User;
import ru.practicum.storage.UserRepository;

import java.util.ArrayList;
import java.util.List;

import static ru.practicum.mapper.UserMapper.dtoToUser;
import static ru.practicum.mapper.UserMapper.userToDto;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;

    public List<UserDto> getUsers(List<Long> ids, Integer from, Integer size) {
        Pageable page = PageRequest.of(from / size, size);
        Page<User> userPage = userRepository.findByIdIn(ids,page);

        List<UserDto> result = new ArrayList<>();
        userPage.getContent().forEach(user -> {
            result.add(userToDto(user));
        });

        if (result.size() > from % size) {
            return result.subList(from % size, result.size());
        } else return new ArrayList<>();

    }

    public UserDto saveUser (UserDto userDto){
        User user = userRepository.save(dtoToUser(userDto));
        userDto.setId(user.getId());
        return userDto;
    }

    public void deleteUser (Long id){
        userRepository.deleteById(id);
    }


//
//    public CategoryDto postCategory(CategoryDto categoryDto) {
//        return categoryToDto(categoryRepository.save(dtoToCategory(categoryDto)));
//
//    }
//
//    public void deleteCategory(Long id) {
//        categoryRepository.deleteById(id);
//    }
//
//    public CategoryDto patchCategory(Long catId, CategoryDto categoryDto) {
//
//        Category category = dtoToCategory(categoryDto);
//        category.setId(catId);
//        return categoryToDto(categoryRepository.save(category));
//    }

}
