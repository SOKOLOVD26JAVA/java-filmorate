package ru.yandex.practicum.filmorate.service.FilmsAndUsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.storage.InMemoryUserStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final InMemoryUserStorage inMemoryUserStorage;

    @Autowired
    public UserService(InMemoryUserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    public User addToFriends(int userId, int friendId) {
        if (checkUsers(userId, friendId)) {
            User user = inMemoryUserStorage.getUsers().get(userId);
            User secondUser = inMemoryUserStorage.getUsers().get(friendId);
            user.getFriendList().add(friendId);
            secondUser.getFriendList().add(userId);
            return user;
        } else {
            throw new NotFoundException("Проверьте вводимые данные.");
        }
    }
// Почему - то с такой проверкой постман не пускает.((
    public User deleteFriendFromUser(int id, int friendId) {
        if (checkUsers(id, friendId)) {
//            if (!inMemoryUserStorage.getUsers().get(id).getFriendList().contains(friendId)) {
//                throw new NotFoundException("У пользователя с ID " + id +
//                        " в друзьях отсутствует пользователь с ID " + friendId + ".");
//            } else {
                User user = inMemoryUserStorage.getUsers().get(id);
                User secondUser = inMemoryUserStorage.getUsers().get(friendId);
                user.getFriendList().remove(friendId);
                secondUser.getFriendList().remove(id);
                return user;
//            }
        }
        throw new NotFoundException("Проверьте вводимые данные.");
    }

    public Collection<User> getUserFriends(int userId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)){
            throw new NotFoundException("Пользователь с ID "+userId+" отсутствует");
        }
        User user = inMemoryUserStorage.getUsers().get(userId);
        Collection<User> friendsId = user.getFriendList().stream().map(id -> inMemoryUserStorage
                .getUsers().get(id)).toList();
        return friendsId;
    }

    private boolean checkUsers(int userId, int friendId) {
        if (!inMemoryUserStorage.getUsers().containsKey(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден.");
        } else if (!inMemoryUserStorage.getUsers().containsKey(friendId)) {
            throw new NotFoundException("Пользователь с ID " + friendId + " не найден.");
        } else {
            return true;
        }
    }
}
