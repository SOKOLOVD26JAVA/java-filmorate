package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.dal.UserRepository;
import ru.yandex.practicum.filmorate.dto.UserDto;

import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;


import java.util.*;
import java.util.stream.Collectors;


@Slf4j
@Service
public class UserService {

    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public static void userValidation(User user) {

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.warn("При добавлении у пользователя с ID: {} отсутствует поле name. ", user.getId());
            user.setName(user.getLogin());
            log.info("В поле name записано поле login. Новое значение: {}", user.getName());
        }
    }

    public List<UserDto> getUsers() {
        log.info("Попытка получения всех пользователей.");
        return userRepository.findAll().stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }

    public List<UserDto> getFriends(int userId) {
        log.info("Попытка получения списка друзей у пользователя.");
        log.info("Проверка существования пользователя с ID: {} для получения френдлиста", userId);
        User user = userRepository.findById(userId);

        return userRepository.getFriends(userId).stream().map(UserMapper::mapToUserDto)
                .collect(Collectors.toList());

    }

    public UserDto createUser(UserDto userDto) {
        log.info("Попытка создания пользователя.");
        User user = UserMapper.mapToUser(userDto);
        userValidation(user);
        User savedUser = userRepository.saveUser(user);
        return UserMapper.mapToUserDto(savedUser);

    }

    public UserDto updateUser(UserDto userDto) {
        log.info("Попытка обновления пользователя");
        log.info("Проверка существования пользователя с ID: {} для обновления", userDto.getId());
        User findUser = userRepository.findById(userDto.getId());

        User userForUpdate = UserMapper.mapToUser(userDto);
        User updatedUser = userRepository.updateUser(userForUpdate);
        userValidation(updatedUser);
        return UserMapper.mapToUserDto(updatedUser);
    }

    public void addToFriends(int userId, int friendId) {
        log.info("Попытка добавления пользователя с ID: {} в друзья к пользователю с ID: {}", userId, friendId);
        log.info("Проверка существования пользователя с ID: {} для добавления в друзья.", userId);
        User findUser = userRepository.findById(userId);
        log.info("Проверка существования пользователя с ID: {} (добавляемый друг). ", friendId);
        User findUser2 = userRepository.findById(friendId);

        userRepository.addToFriends(userId, friendId);
    }

    public void deleteUser(int userId) {
        log.info("Попытка удаления пользователя.");
        log.info("Проверка существования пользователя с ID: {} для удаления.", userId);
        User findUser = userRepository.findById(userId);

        userRepository.deleteUser(userId);
    }

    public void deleteFromFriendList(int userId, int friendId) {
        log.info("Попытка удаления пользователя с ID: {} из друзей у пользователю с ID: {}", userId, friendId);
        log.info("Проверка существования пользователя с ID: {} для удаления из друзей.", userId);
        User findUser = userRepository.findById(userId);
        log.info("Проверка существования пользователя с ID: {} (удаляемый друг). ", friendId);
        User findUser2 = userRepository.findById(friendId);

        userRepository.deleteFromFriendList(userId, friendId);
    }

    public List<UserDto> getCommonFriends(int userId, int friendId) {
        log.info("Попытка получения общих друзей между пользователями с ID: {} и {}", userId, friendId);
        log.info("Проверка существования пользователя с ID: {} для получения списка общих друзей.", userId);
        User findUser = userRepository.findById(userId);
        log.info("Проверка существования пользователя с ID: {} . ", friendId);
        User findUser2 = userRepository.findById(friendId);

        return userRepository.getCommonFriends(userId, friendId).stream().map(UserMapper::mapToUserDto).collect(Collectors.toList());
    }


}
