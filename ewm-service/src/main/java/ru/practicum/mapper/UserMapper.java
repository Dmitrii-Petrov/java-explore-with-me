package ru.practicum.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.DTO.UserDto;
import ru.practicum.DTO.UserShortDto;
import ru.practicum.model.User;

@Component
@AllArgsConstructor
public class UserMapper {

    public static UserDto userToDto(User user) {
        return new UserDto(user.getId(), user.getName(), user.getEmail());
    }

    public static UserShortDto userToShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }

    public static User dtoToUser(UserDto userDto) {
        User user = new User();
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        return user;
    }
}
