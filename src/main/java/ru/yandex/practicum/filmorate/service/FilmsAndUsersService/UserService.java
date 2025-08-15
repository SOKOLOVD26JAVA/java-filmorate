package ru.yandex.practicum.filmorate.service.FilmsAndUsersService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addToFriends(int userId, int friendId) {
        log.info("Попытка добавления пользователя с ID: {} в друзья к пользователю с ID: {}.", userId, friendId);
        if (checkUsers(userId, friendId)) {
            User user = inMemoryUserStorage.getUsers().get(userId);
            User secondUser = inMemoryUserStorage.getUsers().get(friendId);
            user.getFriendList().add(friendId);
            secondUser.getFriendList().add(userId);
            log.info("Пользователи с ID: {} и {} добавлены в друзья.", userId, friendId);
            return user;
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }

    // Почему - то с такой проверкой постман не пускает.((
    public User deleteFriendFromUser(int id, int friendId) {
        log.info("Попытка удаления пользователя с ID: {} из друзей у  пользователя с ID: {}.", id, friendId);
        if (checkUsers(id, friendId)) {
//            if (!inMemoryUserStorage.getUsers().get(id).getFriendList().contains(friendId)) {
//                throw new NotFoundException("У пользователя с ID " + id +
//                        " в друзьях отсутствует пользователь с ID " + friendId + ".");
//            } else {
            User user = inMemoryUserStorage.getUsers().get(id);
            User secondUser = inMemoryUserStorage.getUsers().get(friendId);
            user.getFriendList().remove(friendId);
            secondUser.getFriendList().remove(id);
            log.info("Пользователь с ID: {} успешно удален из друзей у пользователя с ID: {}. ", friendId, id);
            return user;
//            }
        }
        throw new NotFoundException("Проверьте вводимые данные.");
    }

    public Collection<User> getUserFriends(int userId) {
        log.info("Попытка получения списка друзей у пользователя с ID: {}.", userId);
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            log.warn("Пользователь с ID: {} отсутствует.", userId);
            throw new NotFoundException("Пользователь с ID " + userId + " отсутствует.");
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        Collection<User> friendsId = user.getFriendList().stream().map(id -> inMemoryUserStorage
                .getUsers().get(id)).toList();
        log.info("Список друзей пользователя с ID: {} получен.", userId);
        return friendsId;
    }

    public Collection<User> getCommonFriends(int id, int otherId) {
        log.info("Попытка получения общих друзей у пользователей с ID: {} и {}.", id, otherId);
        checkUsers(id, otherId);
        Map<Integer, Integer> countMap = new HashMap<>();
        Collection<Integer> allFriends = new ArrayList<>();
        if (inMemoryUserStorage.getUsers().get(id).getFriendList() != null) {
            allFriends.addAll(inMemoryUserStorage.getUsers().get(id).getFriendList());
        } else {
            log.warn("Список друзей пользователя с ID: {} пуст.", id);
            throw new NotFoundException("Список друзей пользователя с ID: " + id + " пуст.");
        }
        if (inMemoryUserStorage.getUsers().get(otherId).getFriendList() != null) {
            allFriends.addAll(inMemoryUserStorage.getUsers().get(otherId).getFriendList());
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
                commonFriends.add(inMemoryUserStorage.getUsers().get(entry.getKey()));
            }
        }
        log.info("Список общих друзей возвращен.");
        return commonFriends;
    }

    private boolean checkUsers(int userId, int friendId) {
        log.info("Проверка пользователей пл спискам.");
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден.");
        } else {
            return true;
        }
    }
}
