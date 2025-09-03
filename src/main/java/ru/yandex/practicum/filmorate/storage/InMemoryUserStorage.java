package ru.yandex.practicum.filmorate.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static ru.yandex.practicum.filmorate.service.UserService.userValidation;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {

    protected int id = 1;
    protected Map<Integer, User> users = new HashMap<>();


    @Override
    public Collection<User> getUsers() {
        return users.values();
    }

    @Override
    public User addUser(User user) {
        log.info("Попытка добавления нового пользователя, login: {} ", user.getLogin());
        user.setId(generateID());
        log.info("Для пользователя сгенерирован ID: {}.", user.getId());
        userValidation(user);
        users.put(user.getId(), user);
        log.info("Пользователь с именем: {} успешно добавлен.", user.getName());
        return user;
    }

    @Override
    public User updateUser(User newUser) {
        log.info("Попытка обновления пользователя, login: {}", newUser.getLogin());
        userValidation(newUser);
        if (users.containsKey(newUser.getId())) {
            users.put(newUser.getId(), newUser);
            log.info("Пользователь с логином: {} успешно обновлен", newUser.getLogin());
            return newUser;
        } else {
            log.warn("Пользователь: {} с ID: {} отсутствует в списке.", newUser.getName(), newUser.getId());
            throw new NotFoundException("Такого пользователя нет.");
        }
    }

    @Override
    public Collection<User> delete(User user) {
        log.info("Попытка удаления пользователя с ID: {}", user.getId());
        userValidation(user);
        if (users.containsKey(user.getId())) {
            users.remove(user.getId());
            log.info("Пользователь с ID: {} удален.", user.getId());
            return users.values();
        } else {
            throw new NotFoundException("Такого фильма нет");
        }
    }

    @Override
    public User getUser(int id) {
        return users.get(id);
    }

    private int generateID() {
        return id++;
    }

}
