package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public Collection<User> deleteUser(User user) {
        return userStorage.delete(user);
    }

    public User addToFriends(int userId, int friendId) {
        log.info("Попытка добавления пользователя с ID: {} в друзья к пользователю с ID: {}.", userId, friendId);
        if (checkUser(userId) && checkUser(friendId)) {
            User user = userStorage.getUser(userId);
            User secondUser = userStorage.getUser(friendId);
            user.getFriendList().add(friendId);
            secondUser.getFriendList().add(userId);
            log.info("Пользователи с ID: {} и {} добавлены в друзья.", userId, friendId);
            return user;
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }

    public User deleteFriendFromUser(int userId, int friendId) {
        log.info("Попытка удаления пользователя с ID: {} из друзей у  пользователя с ID: {}.", userId, friendId);
        if (checkUser(userId) && checkUser(friendId)) {
            User user = userStorage.getUser(userId);
            User secondUser = userStorage.getUser(friendId);
            user.getFriendList().remove(friendId);
            secondUser.getFriendList().remove(userId);
            log.info("Пользователь с ID: {} успешно удален из друзей у пользователя с ID: {}. ", friendId, userId);
            return user;
        }
        throw new NotFoundException("Проверьте вводимые данные.");
    }

    public Collection<User> getUserFriends(int userId) {
        log.info("Попытка получения списка друзей у пользователя с ID: {}.", userId);
        checkUser(userId);
        User user = userStorage.getUser(userId);
        Collection<User> friendsId = user.getFriendList().stream().flatMap(friendId -> userStorage.getUsers().stream().filter(user1 -> user1.getId() == friendId)).toList();
        log.info("Список друзей пользователя с ID: {} получен.", userId);
        return friendsId;
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        log.info("Попытка получения общих друзей у пользователей с ID: {} и {}.", id, otherId);
        checkUser(id);
        checkUser(otherId);
        User user = userStorage.getUser(id);
        User secondUser = userStorage.getUser(otherId);
        return user.getFriendList().stream().filter(friendId -> secondUser.getFriendList().contains(friendId))
                .map(userStorage::getUser).toList();

    }

    private boolean checkUser(int userId) {
        log.info("Проверка пользователя по списку.");
        if (userStorage.getUser(userId) == null) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else {
            return true;
        }
    }

    public static void userValidation(User user) {

        if (user.getName() == null || user.getName().isEmpty() || user.getName().isBlank()) {
            log.warn("При добавлении у пользователя с ID: {} отсутствует поле name. ", user.getId());
            user.setName(user.getLogin());
            log.info("В поле name записано поле login. Новое значение: {}", user.getName());
        }
    }
}
