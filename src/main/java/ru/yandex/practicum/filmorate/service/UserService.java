package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public User addToFriends(int userId, int friendId) {
        log.info("Попытка добавления пользователя с ID: {} в друзья к пользователю с ID: {}.", userId, friendId);
        if (checkUsers(userId, friendId)) {
            User user = userStorage.getUsers().stream().filter(user1 -> user1.getId() == userId).findFirst().orElse(null);
            User secondUser = userStorage.getUsers().stream().filter(user1 -> user1.getId() == friendId).findFirst().orElse(null);
            user.getFriendList().add(friendId);
            secondUser.getFriendList().add(userId);
            log.info("Пользователи с ID: {} и {} добавлены в друзья.", userId, friendId);
            return user;
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }

    // Почему - то с такой проверкой постман не пускает.((
    public User deleteFriendFromUser(int userId, int friendId) {
        log.info("Попытка удаления пользователя с ID: {} из друзей у  пользователя с ID: {}.", userId, friendId);
        if (checkUsers(userId, friendId)) {
//            if (!inMemoryUserStorage.getUsers().get(id).getFriendList().contains(friendId)) {
//                throw new NotFoundException("У пользователя с ID " + id +
//                        " в друзьях отсутствует пользователь с ID " + friendId + ".");
//            } else {
            User user = userStorage.getUsers().stream().filter(user1 -> user1.getId() == userId).findFirst().orElse(null);
            User secondUser = userStorage.getUsers().stream().filter(user1 -> user1.getId() == friendId).findFirst().orElse(null);
            user.getFriendList().remove(friendId);
            secondUser.getFriendList().remove(userId);
            log.info("Пользователь с ID: {} успешно удален из друзей у пользователя с ID: {}. ", friendId, userId);
            return user;
//            }
        }
        throw new NotFoundException("Проверьте вводимые данные.");
    }

    public Collection<User> getUserFriends(int userId) {
        log.info("Попытка получения списка друзей у пользователя с ID: {}.", userId);
        Optional<User> findUser = userStorage.getUsers().stream().filter(user -> user.getId() == userId).findFirst();
        if (findUser.isEmpty()) {
            log.warn("Пользователь с ID: {} отсутствует.", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " отсутствует.");
        } else {
            User user = findUser.get();
            Collection<User> friendsId = user.getFriendList().stream().flatMap(friendId -> userStorage.getUsers().stream().filter(user1 -> user1.getId() == friendId)).toList();
            log.info("Список друзей пользователя с ID: {} получен.", userId);
            return friendsId;
        }
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        log.info("Попытка получения общих друзей у пользователей с ID: {} и {}.", id, otherId);
        checkUsers(id, otherId);
        Map<Integer, Integer> countMap = new HashMap<>();
        Collection<Integer> allFriends = new ArrayList<>();
        Optional<User> mainUser = userStorage.getUsers().stream().filter(user -> user.getId() == id).findFirst();
        Optional<User> otherUser = userStorage.getUsers().stream().filter(user -> user.getId() == otherId).findFirst();
        if (!mainUser.get().getFriendList().isEmpty()) {
            allFriends.addAll(mainUser.get().getFriendList());
        } else {
            log.warn("Список друзей пользователя с ID: {} пуст.", id);
            throw new NotFoundException("Список друзей пользователя с ID: " + id + " пуст.");
        }
        if (!otherUser.get().getFriendList().isEmpty()) {
            allFriends.addAll(otherUser.get().getFriendList());
            log.info("Списки друзей объеденины.");
        } else {
            log.warn("Список друзей пользователя с ID: {} пуст.", otherId);
            throw new NotFoundException("Список друзей пользователя с ID: " + otherId + " пуст.");
        }
        for (Integer userID : allFriends) {
            countMap.put(userID, countMap.getOrDefault(userID, 0) + 1);
        }
        Collection<User> commonFriends = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : countMap.entrySet()) {
            if (entry.getValue() > 1) {
                Integer friendId = entry.getKey();
                userStorage.getUsers().stream()
                        .filter(user -> user.getId() == friendId)
                        .findFirst()
                        .ifPresent(commonFriends::add);
            }
        }
        log.info("Список общих друзей возвращен.");
        return commonFriends;
    }

    private boolean checkUsers(int userId, int friendId) {
        log.info("Проверка пользователей пл спискам.");
        Optional<User> findUser = userStorage.getUsers().stream().filter(user -> user.getId() == userId).findFirst();
        Optional<User> findFriend = userStorage.getUsers().stream().filter(user -> user.getId() == friendId).findFirst();
        if (findUser.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else if (findFriend.isEmpty()) {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден.");
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
